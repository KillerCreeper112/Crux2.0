package killercreepr.cruxblocks;

import killercreepr.crux.Crux;
import killercreepr.crux.block.CruxBlockWrapper;
import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.data.entity.PlayerMemory;
import killercreepr.crux.data.tick.ManagedTicked;
import killercreepr.crux.handler.BlockHandler;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.crux.util.CruxEntity;
import killercreepr.crux.util.CruxLoc;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.data.CustomBlockData;
import killercreepr.cruxblocks.block.data.events.CustomBlockDataRemoveEvent;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.block.wrapper.CruxBlockCruxWrapper;
import killercreepr.cruxblocks.block.wrapper.CruxGroupBlockWrapper;
import killercreepr.cruxblocks.command.CruxBlocksCommands;
import killercreepr.cruxblocks.config.CruxConfigHook;
import killercreepr.cruxblocks.data.entity.MinerHolder;
import killercreepr.cruxblocks.item.CruxItemsItemProvider;
import killercreepr.cruxblocks.item.KeyedItemProvider;
import killercreepr.cruxblocks.listener.CustomBlockClientSyncListener;
import killercreepr.cruxblocks.listener.CustomBlocksListener;
import killercreepr.cruxblocks.listener.NoteBlockSoundsListener;
import killercreepr.cruxblocks.manager.CruxBlockManager;
import killercreepr.cruxblocks.registeries.CruxBlockRegistry;
import killercreepr.cruxblocks.registeries.CruxBlocksRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CruxBlocksModule implements CruxModule, CruxBlockManager, BlockHandler {
    public static final String NAMESPACE = StandardModules.CRUX_BLOCKS;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    protected final @NotNull CruxBlockRegistry blockRegistry;
    public CruxBlocksModule(@NotNull CruxBlockRegistry blockRegistry) {
        this.blockRegistry = blockRegistry;
    }

    public CruxBlocksModule() {
        this(CruxBlocksRegistries.BLOCKS);
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
    public void reload(@NotNull CruxPlugin plugin) {
        if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_CONFIGS)){
            CruxConfigHook.loadCfgBlockGroups(plugin, "blocks");
        }
    }

    @Override
    public void onLoad(@NotNull CruxPlugin plugin) {
        if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_CONFIGS)){
            CruxConfigHook.register();
        }
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        Crux.handlers().setBlock(this);
        plugin.registerListeners(
            new CustomBlocksListener(plugin, this),
            new NoteBlockSoundsListener(plugin, CruxBlocksRegistries.BLOCKS),
            new CustomBlockClientSyncListener(plugin, CruxBlocksRegistries.BLOCKS)
        );

        EntityMemory.registerFunction(plugin, e ->{
            if(!(e instanceof PlayerMemory p)) return;
            p.getDataHolders().register(new MinerHolder(p, this));
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

    public @NotNull CruxBlockRegistry getBlockRegistry() {
        return blockRegistry;
    }

    @Override
    public @Nullable ActiveCruxBlock getActiveBlock(@NotNull Block at){
        return getActiveBlock(at, at.getBlockData());
    }

    @Override
    public @Nullable ActiveCruxBlock getActiveBlock(@NotNull Block at, @NotNull BlockData data) {
        if(hasTickedBlock(at)) return activeBlocks.get(at);
        CruxBlock block = blockRegistry.getByBlockData(data);
        if(block==null) return null;
        ActiveCruxBlock active = block.createActive(at);

        if(active instanceof ManagedTicked ticked){
            activeBlocks.put(active.getBlock(), active);
            ticked.started();
        }

        return active;
    }

    @Override
    public boolean hasTickedBlock(@NotNull Block at){
        return activeBlocks.containsKey(at);
    }

    @Override
    public @Nullable ActiveCruxBlock getTickedBlock(@NotNull Block at){
        return activeBlocks.get(at);
    }

    public @NotNull BukkitRunnable buildBlockTickTask(@NotNull Server server){
        return new BukkitRunnable() {
            private int checkBlocks = 40;
            @Override
            public void run() {
                if(checkBlocks > 0){
                    checkBlocks--;
                    if(checkBlocks <= 0){
                        checkBlocks = 40;
                        for(Player p : server.getOnlinePlayers()){
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
                        CruxEntity.getEntityAmountNearChunk(a.getBlock().getChunk(), 3, e -> e instanceof Player) < 1){
                        t.stopped();
                        activeBlocks.remove(a.getBlock());
                        continue;
                    }
                    t.tick();
                }
            }
        };
    }

    @Override
    public @NotNull Block setType(@NotNull Block b, @NotNull Material m, boolean applyPhysics, boolean removeTags) {
        b.setType(m, applyPhysics);
        if(removeTags){
            if(callRemoveBlockDataEvent(b, null)){
                new CustomBlockData(b, Crux.getMainPlugin()).clear();
            }
        }
        return b;
    }

    @Override
    public @Nullable CruxBlockWrapper getBlock(@NotNull Key key) {
        CruxBlockGroup group = blockRegistry.getGroup(key);
        if(group != null){
            return new CruxGroupBlockWrapper(group);
        }
        CruxBlock block = blockRegistry.get(key);
        if(block != null){
            return new CruxBlockCruxWrapper(block);
        }
        Material material = Registry.MATERIAL.get(key);
        if(material==null) return null;
        return new CruxBlockWrapper.Vanilla(material);
    }

    public static boolean callRemoveBlockDataEvent(@NotNull Block block, @Nullable Event bukkitEvent) {
        if(!CustomBlockData.hasCustomBlockData(block, Crux.getMainPlugin()) || CustomBlockData.isProtected(block, Crux.getMainPlugin())) {
            return false;
        }
        return new CustomBlockDataRemoveEvent(Crux.getMainPlugin(), block, bukkitEvent).callEvent();
    }
}
