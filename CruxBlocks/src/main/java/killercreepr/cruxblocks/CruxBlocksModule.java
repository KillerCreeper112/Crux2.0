package killercreepr.cruxblocks;

import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.data.entity.PlayerMemory;
import killercreepr.crux.data.tick.ManagedTicked;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.crux.util.CruxEntity;
import killercreepr.crux.util.CruxLoc;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.command.CruxBlocksCommands;
import killercreepr.cruxblocks.data.entity.MinerHolder;
import killercreepr.cruxblocks.item.CruxItemsItemProvider;
import killercreepr.cruxblocks.item.KeyedItemProvider;
import killercreepr.cruxblocks.listener.CustomBlocksListener;
import killercreepr.cruxblocks.manager.CruxBlockManager;
import killercreepr.cruxblocks.registeries.CruxBlocksRegistries;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CruxBlocksModule implements CruxModule, CruxBlockManager {
    public static final String NAMESPACE = "CruxBlocks";
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    protected final Map<Block, ActiveCruxBlock> activeBlocks = new HashMap<>();
    protected @Nullable KeyedItemProvider keyedItemProvider;

    public Map<Block, ActiveCruxBlock> getActiveBlocks() {
        return activeBlocks;
    }

    public @Nullable KeyedItemProvider getKeyedItemProvider() {
        return keyedItemProvider;
    }

    public void setKeyedItemProvider(@Nullable KeyedItemProvider keyedItemProvider) {
        this.keyedItemProvider = keyedItemProvider;
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        plugin.registerListeners(
            new CustomBlocksListener(this)
        );

        EntityMemory.registerFunction(plugin, e ->{
            if(!(e instanceof PlayerMemory p)) return;
            p.getHolders().register(new MinerHolder(p, this));
        });

        CruxBlocksCommands.register(plugin);

        if(CruxRegistries.MODULES.containsKey("CruxItems")){
            keyedItemProvider = new CruxItemsItemProvider();
        }else keyedItemProvider = null;
    }

    @Override
    public void onDisable(@NotNull CruxPlugin plugin) {
        CruxModule.super.onDisable(plugin);
        activeBlocks.values().forEach(b ->{
            if(!(b instanceof ManagedTicked ticked)) return;
            ticked.stopped();
        });
        activeBlocks.clear();
    }

    @Override
    public @Nullable ActiveCruxBlock getActiveBlock(@NotNull Block at){
        return getActiveBlock(at, at.getBlockData());
    }

    @Override
    public @Nullable ActiveCruxBlock getActiveBlock(@NotNull Block at, @NotNull BlockData data) {
        if(hasTickedBlock(at)) return activeBlocks.get(at);
        CruxBlock block = CruxBlocksRegistries.BLOCKS.getByBlockData(data);
        return block==null?null:block.createActive(at);
    }

    @Override
    public boolean hasTickedBlock(@NotNull Block at){
        return activeBlocks.containsKey(at);
    }

    @Override
    public @Nullable ActiveCruxBlock getTickedBlock(@NotNull Block at){
        return activeBlocks.get(at);
    }

    public @NotNull BukkitRunnable blockTick(){
        return new BukkitRunnable() {
            private int checkBlocks = 40;
            @Override
            public void run() {
                if(checkBlocks > 0){
                    checkBlocks--;
                    if(checkBlocks <= 0){
                        checkBlocks = 40;
                        for(Player p : Bukkit.getOnlinePlayers()){
                            if(!p.getChunk().isLoaded()) continue;
                            for(Block b : CruxLoc.getNearbyBlocks(p.getLocation().getBlock(), 24)){
                                if(!b.getChunk().isLoaded()) continue;
                                if(hasTickedBlock(b)) continue;
                                ActiveCruxBlock active = getActiveBlock(b);
                                if(active instanceof ManagedTicked ticked){
                                    activeBlocks.put(active.getBlock(), active);
                                    ticked.started();
                                }
                            }
                        }
                    }
                }

                for(ActiveCruxBlock a : new HashSet<>(activeBlocks.values())){
                    if(!(a instanceof ManagedTicked t)) continue;
                    if(!a.isValid() || t.shouldStop() || !a.getBlock().getChunk().isLoaded() ||
                        CruxEntity.getEntitiesNear(Player.class, a.getBlock().getLocation(), 32f, null).isEmpty()){
                        t.stopped();
                        activeBlocks.remove(a.getBlock());
                        continue;
                    }
                    t.tick();
                }
            }
        };
    }
}
