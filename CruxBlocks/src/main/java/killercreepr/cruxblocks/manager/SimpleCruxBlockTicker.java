package killercreepr.cruxblocks.manager;

import killercreepr.crux.Crux;
import killercreepr.crux.data.tick.ManagedTicked;
import killercreepr.crux.game.SimpleStatutable;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxTickedBlock;
import killercreepr.cruxblocks.block.data.CustomBlockData;
import killercreepr.cruxblocks.registry.CruxBlockRegistry;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class SimpleCruxBlockTicker extends SimpleStatutable implements CruxBlockTicker, Listener {
    protected final Plugin plugin;
    protected final CruxBlockRegistry blockRegistry;
    public SimpleCruxBlockTicker(Plugin plugin, CruxBlockRegistry blockRegistry) {
        this.plugin = plugin;
        this.blockRegistry = blockRegistry;
    }

    protected final Map<Block, ActiveCruxBlock> active = new ConcurrentHashMap<>();
    protected final Map<Long, Collection<ActiveCruxBlock>> chunkToActive = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();
        CustomBlockData.getBlocksWithCustomData(chunk, ActiveCruxTickedBlock.CUSTOM_TICKED_KEY).forEach(b ->{
            getActiveBlock(b);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        Chunk chunk = event.getChunk();
        Collection<ActiveCruxBlock> active = this.chunkToActive.remove(chunk.getChunkKey());
        if(active == null) return;
        active.forEach(block ->{
            ActiveCruxBlock removed = this.active.remove(block.getBlock());
            if(!(removed instanceof ManagedTicked ticked)){
                Crux.log(Level.WARNING, block.getBlock() + " was supposed to be an active ticked block but was not registered properly!");
                return;
            }
            ticked.stopped();
        });
    }


    private void addActive(ActiveCruxBlock active){
        addActive(active, active.getBlock().getChunk());
    }

    private void addActive(ActiveCruxBlock active, Chunk chunk){
        this.active.put(active.getBlock(), active);
        this.chunkToActive.computeIfAbsent(chunk.getChunkKey(), (l) -> new ArrayList<>()).add(active);
    }

    @Override
    public @Nullable ActiveCruxBlock getActiveBlock(@NotNull Block at, @NotNull BlockData data) {
        if(hasTickedBlock(at)) return active.get(at);
        CruxBlock block = blockRegistry.getByBlockData(at, data);
        if(block==null) return null;
        ActiveCruxBlock active = block.createActive(at);

        if(active instanceof ManagedTicked ticked){
            addActive(active);
            ticked.started();
        }

        return active;
    }

    @Override
    public @Nullable Collection<ActiveCruxBlock> getActiveBlocks(@NotNull Chunk chunk) {
        return chunkToActive.get(chunk.getChunkKey());
    }

    @Override
    public @Nullable ActiveCruxBlock getActiveBlock(@NotNull Block at) {
        return getActiveBlock(at, at.getBlockData());
    }

    @Override
    public boolean hasTickedBlock(@NotNull Block at){
        return active.containsKey(at);
    }

    @Override
    public @Nullable ActiveCruxBlock getTickedBlock(@NotNull Block at){
        return active.get(at);
    }

    @Override
    public void started() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        new BukkitRunnable(){
            @Override
            public void run() {
                if(!isActive()){
                    cancel();
                    return;
                }
                tick();
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 1L);
    }

    @Override
    public void stopped() {
        super.stopped();
        HandlerList.unregisterAll(plugin);
        active.values().forEach(b ->{
            if(!(b instanceof ManagedTicked ticked)) return;
            ticked.stopped();
        });
        active.clear();
        chunkToActive.clear();
    }

    protected void removeFromChunk(ActiveCruxBlock b){
        chunkToActive.computeIfPresent(b.getBlock().getChunk().getChunkKey(), (key, list) ->{
            list.remove(b);
            return list.isEmpty() ? null : list;
        });
    }

    @Override
    public void tick() {
        active.values().removeIf(a ->{
            if(!(a instanceof ManagedTicked t)){
                removeFromChunk(a);
                return true;
            }
            if(!a.isValid() || t.shouldStop() || !a.getBlock().getChunk().isLoaded()){
                t.stopped();
                removeFromChunk(a);
                return true;
            }
            t.tick();
            return false;
        });
    }
}
