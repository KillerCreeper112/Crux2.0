package killercreepr.cruxblocks;

import killercreepr.crux.Crux;
import killercreepr.crux.block.BukkitCruxedBlock;
import killercreepr.crux.block.CruxBlockWrapper;
import killercreepr.crux.block.CruxedBlock;
import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.data.entity.PlayerMemory;
import killercreepr.crux.handler.BlockHandler;
import killercreepr.crux.item.dynamic.components.persistence.DynamicPersistentTag;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.CruxCruxedBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.data.CustomBlockData;
import killercreepr.cruxblocks.block.data.events.CustomBlockDataRemoveEvent;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.block.wrapper.CruxBlockCruxWrapper;
import killercreepr.cruxblocks.block.wrapper.CruxGroupBlockWrapper;
import killercreepr.cruxblocks.command.CruxBlocksCommands;
import killercreepr.cruxblocks.components.persistence.BlocksDynamicPersistence;
import killercreepr.cruxblocks.config.CruxConfigHook;
import killercreepr.cruxblocks.config.handler.component.CfgBlockComponents;
import killercreepr.cruxblocks.data.entity.MinerHolder;
import killercreepr.cruxblocks.hook.CruxStructuresHook;
import killercreepr.cruxblocks.item.CruxItemsItemProvider;
import killercreepr.cruxblocks.item.KeyedItemProvider;
import killercreepr.cruxblocks.listener.CustomBlockClientSyncListener;
import killercreepr.cruxblocks.listener.CustomBlocksListener;
import killercreepr.cruxblocks.listener.NoteBlockSoundsListener;
import killercreepr.cruxblocks.manager.CruxBlockManager;
import killercreepr.cruxblocks.manager.CruxBlockTicker;
import killercreepr.cruxblocks.registries.CruxBlockRegistry;
import killercreepr.cruxblocks.registries.CruxBlocksRegistries;
import killercreepr.cruxconfig.config.bukkit.handler.BukkitCfgHandlers;
import killercreepr.cruxconfig.config.bukkit.handler.impl.item.component.persistence.BaseSimplePersistentParser;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxBlocksModule implements CruxModule, CruxBlockManager, BlockHandler {
    public static final String NAMESPACE = StandardModules.CRUX_BLOCKS;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    protected final @NotNull CruxBlockRegistry blockRegistry;
    protected final @NotNull CruxBlockTicker blockTicker;
    public CruxBlocksModule(@NotNull CruxBlockRegistry blockRegistry, @NotNull CruxBlockTicker blockTicker) {
        this.blockRegistry = blockRegistry;
        this.blockTicker = blockTicker;
    }

    public CruxBlocksModule(@NotNull CruxBlockTicker blockTicker) {
        this(CruxBlocksRegistries.BLOCK, blockTicker);
    }

    protected @Nullable KeyedItemProvider keyedItemProvider;

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
        CfgBlockComponents.register(BukkitCfgHandlers.TYPED_DATA_COMPONENT.typeHandlers());
        if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_CONFIGS)){
            CruxConfigHook.register();
            CfgRegistries.DYNAMIC_PERSIST_TAG_PARSER.register(new BaseSimplePersistentParser<>(Crux.key("block_group")) {
                @Override
                public @NotNull FileElement serializeTypedValue(@NotNull FileContext<?> ctx, @NotNull Object object) {
                    return new FileGeneric(object.toString());
                }

                @Override
                public @Nullable String parseObject(@NotNull FileContext<?> ctx, @NotNull FileObject base, @NotNull FileElement e) {
                    return e.getAsString();
                }

                @Override
                public @NotNull DynamicPersistentTag<Object, Key> getDynamicTag(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
                    return BlocksDynamicPersistence.BLOCK_GROUP;
                }

                @Override
                public @NotNull String defaultTagKey() {
                    return "block_group";
                }
            });
        }
        if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_STRUCTURES)){
            CruxStructuresHook.register();
        }
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        Crux.handlers().setBlock(this);
        blockTicker.setStarted();
        plugin.registerListeners(
            new CustomBlocksListener(plugin, this),
            new NoteBlockSoundsListener(plugin, CruxBlocksRegistries.BLOCK),
            new CustomBlockClientSyncListener(plugin, CruxBlocksRegistries.BLOCK)
        );

        EntityMemory.registerFunction(plugin, e ->{
            if(!(e instanceof PlayerMemory p)) return;
            p.getDataHolders().register(new MinerHolder(p, this));
        });

        CruxBlocksCommands.register(plugin);

        if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_ITEMS)){
            keyedItemProvider = new CruxItemsItemProvider();
        }else keyedItemProvider = null;
    }

    @Override
    public void onDisable(@NotNull CruxPlugin plugin) {
        CruxModule.super.onDisable(plugin);
        blockTicker.setStopped();
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
        return blockTicker.getActiveBlock(at, data);
    }

    @Override
    public boolean hasTickedBlock(@NotNull Block at){
        return blockTicker.hasTickedBlock(at);
    }

    @Override
    public @Nullable ActiveCruxBlock getTickedBlock(@NotNull Block at){
        return blockTicker.getTickedBlock(at);
    }

    @Override
    public @NotNull Block setType(@NotNull Block b, @NotNull Material m, boolean applyPhysics, boolean removeTags) {
        b.setType(m, applyPhysics);
        if(removeTags){
            removeTags(b);
        }
        return b;
    }

    @Override
    public @NotNull Key getType(@NotNull Block block) {
        CruxBlock cruxBlock = getBlockRegistry().getByBlock(block);
        if(cruxBlock == null) return block.getType().key();
        return cruxBlock.key();
    }

    @Override
    public void removeTags(@NotNull Block b) {
        if(callRemoveBlockDataEvent(b, null)){
            new CustomBlockData(b, Crux.getMainPlugin()).clear();
        }
    }

    @Override
    public @Nullable CruxBlockWrapper getBlockWrapper(@NotNull Key key) {
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

    @Override
    public @Nullable CruxedBlock getBlock(@NotNull Block block) {
        ActiveCruxBlock active = getActiveBlock(block);
        if(active == null) return new BukkitCruxedBlock(block);
        return new CruxCruxedBlock(active);
    }

    @Override
    public @NotNull Key getType(@NotNull Block block, @NotNull BlockData data) {
        CruxBlock cruxBlock = getBlockRegistry().getByBlockData(block, data);
        if(cruxBlock == null) return data.getMaterial().key();
        return cruxBlock.key();
    }

    @Override
    public @NotNull Key getType(@NotNull BlockState state) {
        CruxBlock cruxBlock = getBlockRegistry().getByBlockState(state);
        if(cruxBlock == null) return state.getType().key();
        return cruxBlock.key();
    }

    public static boolean callRemoveBlockDataEvent(@NotNull Block block, @Nullable Event bukkitEvent) {
        if(!CustomBlockData.hasCustomBlockData(block, Crux.getMainPlugin()) || CustomBlockData.isProtected(block, Crux.getMainPlugin())) {
            return false;
        }
        return new CustomBlockDataRemoveEvent(Crux.getMainPlugin(), block, bukkitEvent).callEvent();
    }
}
