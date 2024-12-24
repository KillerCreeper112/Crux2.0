package killercreepr.cruxstructures.core;

import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.crux.api.plugin.module.CruxModule;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.plugin.module.StandardModules;
import killercreepr.cruxconfig.config.bukkit.handler.BukkitCfgHandlers;
import killercreepr.cruxconfig.config.bukkit.handler.impl.FileGenericEnum;
import killercreepr.cruxconfig.config.bukkit.handler.impl.component.FileDataComponentType;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.FileSimpleLootTable;
import killercreepr.cruxconfig.config.bukkit.registry.FileDataComponentRegistry;
import killercreepr.cruxconfig.config.bukkit.standard.CommonLootTableHandlers;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxstructures.api.component.StructureComponent;
import killercreepr.cruxstructures.api.component.StructureEditorComponent;
import killercreepr.cruxstructures.api.location.LocationFinder;
import killercreepr.cruxstructures.api.structure.generation.StructureCenter;
import killercreepr.cruxstructures.api.structure.generation.StructureChunkRequirement;
import killercreepr.cruxstructures.api.structure.generation.StructureGenerator;
import killercreepr.cruxstructures.api.structure.generation.StructureRequirement;
import killercreepr.cruxstructures.core.commands.StructureCommands;
import killercreepr.cruxstructures.core.config.*;
import killercreepr.cruxstructures.core.config.generation.*;
import killercreepr.cruxstructures.core.config.location.FileDummyLocationFinder;
import killercreepr.cruxstructures.core.config.location.FileNearbySolidBlockFinder;
import killercreepr.cruxstructures.core.config.module.*;
import killercreepr.cruxstructures.core.manager.StructureManager;
import killercreepr.cruxstructures.core.structure.CfgFAWEStructure;
import killercreepr.cruxstructures.core.structure.component.StructureComponents;
import killercreepr.cruxstructures.core.structure.component.StructureOuterBoxComponent;
import killercreepr.cruxstructures.core.structure.component.StructureStoredBlocksComponent;
import killercreepr.cruxstructures.core.structure.component.StructureTickedStoredComponent;
import killercreepr.cruxstructures.core.structure.module.WallsModule;
import killercreepr.cruxstructures.core.structure.stored.SimpleStoredStructure;
import net.kyori.adventure.key.Key;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxStructuresModule implements CruxModule {
    public static final FileSimpleLootTable<Key> fileSimpleLootTable = CommonLootTableHandlers.KEY;
    public static final String NAMESPACE = StandardModules.CRUX_STRUCTURES;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    protected final FileCfgStructureGen fileCfgStructureGen = new FileCfgStructureGen();
    protected final FileStructureCenter fileStructureCenter = new FileStructureCenter();
    protected final FileStructureRequirement fileStructureRequirement = new FileStructureRequirement();
    protected final FileStructureChunkRequirement fileStructureChunkRequirement = new FileStructureChunkRequirement();
    protected final FileStructureModule fileStructureModule = new FileStructureModule();
    public final FileCfgFAWEStructure fileCfgFAWEStructure = new FileCfgFAWEStructure();

    protected final FileLocationFinder fileLocationFinder = new FileLocationFinder();

    public FileCfgStructureGen getFileCfgStructureGen() {
        return fileCfgStructureGen;
    }

    public FileLocationFinder getFileLocationFinder() {
        return fileLocationFinder;
    }

    public FileCfgFAWEStructure getFileCfgFAWEStructure() {
        return fileCfgFAWEStructure;
    }

    public FileStructureCenter getFileStructureCenter() {
        return fileStructureCenter;
    }

    public FileStructureModule getFileStructureModule() {
        return fileStructureModule;
    }

    public FileStructureRequirement getFileStructureRequirement() {
        return fileStructureRequirement;
    }

    public FileStructureChunkRequirement getFileStructureChunkRequirement() {
        return fileStructureChunkRequirement;
    }

    public void registerCommands(@NotNull CruxPlugin plugin, @NotNull StructureManager structureManager){
        new StructureCommands(plugin, structureManager).register();
    }

    @Override
    public void onLoad(@NotNull CruxPlugin plugin) {
        StructureComponents.register();
        registerCfgComponents(BukkitCfgHandlers.TYPED_DATA_COMPONENT.typeHandlers());
        CfgRegistries.SIMPLE_REGISTRY.forEach(registry -> {
            registry.registerFileHandler(StructureGenerator.class, fileCfgStructureGen);
            registry.registerFileHandler(StructureCenter.class, fileStructureCenter);
            registry.registerFileHandler(StructureRequirement.class, fileStructureRequirement);
            registry.registerFileHandler(StructureChunkRequirement.class, fileStructureChunkRequirement);
            registry.registerFileHandler(StructureComponent.class, fileStructureModule);

            registry.registerFileHandler(CfgFAWEStructure.class, fileCfgFAWEStructure);
            registry.registerFileHandler(LocationFinder.class, fileLocationFinder);
            registry.registerFileHandler(WallsModule.WallRotationType.class, new FileGenericEnum<>(WallsModule.WallRotationType.class));

            registry.registerFileHandler(WallsModule.Wall.class, new FileWall());
            registry.registerFileHandler(WallsModule.WallPart.class, new FileWallPart());
        });

        fileStructureCenter.TYPE_HANDLERS.register("surface_top", new FileSurfaceTopCenter());
        fileStructureCenter.TYPE_HANDLERS.register("random_surface_top", new FileRandomSurfaceTopCenter());
        fileStructureCenter.TYPE_HANDLERS.register("random_surface", new FileRandomSurfaceCenter());
        fileStructureCenter.TYPE_HANDLERS.register("random_surface_determined", new FileRandomSurfaceDeterminedCenter());
        fileStructureCenter.TYPE_HANDLERS.register("random_surface_avoid_trees", new FileRandomSurfaceTopAvoidTreeCenter());

        fileStructureRequirement.TYPE_HANDLERS.register("biome", new FileBiomeRequirement());
        fileStructureRequirement.TYPE_HANDLERS.register("solid_nearby", new FileSolidNearbyRequirement());
        fileStructureRequirement.TYPE_HANDLERS.register("replaceable_nearby", new FileReplaceableNearbyRequirement());
        fileStructureRequirement.TYPE_HANDLERS.register("chance", new FileChanceRequirement());
        fileStructureRequirement.TYPE_HANDLERS.register("replaceable_directions", new FileReplaceableDirectionTestRequirement());

        fileStructureChunkRequirement.TYPE_HANDLERS.register("noise", new FileChunkNoiseRequirement());
        fileStructureChunkRequirement.TYPE_HANDLERS.register("chance", new FileChunkChanceRequirement());
        fileStructureChunkRequirement.TYPE_HANDLERS.register("per_chunks", new FileChunkPerRequirement());

        fileStructureModule.TYPE_HANDLERS.register("corrupt_veins", new FileCorruptedVeinModule());
        fileStructureModule.TYPE_HANDLERS.register("cone_veins", new FileConeVeinModule());
        fileStructureModule.TYPE_HANDLERS.register("walls", new FileWallsModule());
        fileStructureModule.TYPE_HANDLERS.register("structure_scatter", new FileStructureScatterModule());
        fileStructureModule.TYPE_HANDLERS.register("elongate_floor", new FileElongateFloorModule());
        fileStructureModule.TYPE_HANDLERS.register("clear_space", new FileClearSpaceModule());
        fileStructureModule.TYPE_HANDLERS.register("clear_region", new FileClearRegionModule());

        CfgRegistries.JSON_REGISTRY.forEach(registry ->{
            registry.registerFileHandler(SimpleStoredStructure.class, new FileSimpleStoredStructure<SimpleStoredStructure>());
            //registry.registerFileHandler(CfgStoredStructure.class, new FileCfgStoredStructure<>());
        });

        fileLocationFinder.TYPE_HANDLERS.register("nearby_solid_block", new FileNearbySolidBlockFinder());
        fileLocationFinder.TYPE_HANDLERS.register("dummy", new FileDummyLocationFinder());

        //CfgStructure
        fileCfgStructureGen.typeHandlers().register("set_location_table", new FileLocationSetTableStructureGen());
        fileCfgStructureGen.typeHandlers().register("instant_set_location_table", new FileInstantLocationSetTableStructureGen());

        fileCfgStructureGen.typeHandlers().register("set_location_list", new FileLocationSetListStructureGen());
        fileCfgStructureGen.typeHandlers().register("instant_set_location_list", new FileInstantLocationSetListStructureGen());
    }

    public void registerCfgComponents(FileDataComponentRegistry reg){
        reg.register("structure/outer_box", new FileDataComponentType<StructureComponent>() {
            @Override
            public @Nullable TypedDataComponent<StructureComponent> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                FileRegistry r = ctx.getRegistry();
                Vector expansion = r.deserializeFromFile(Vector.class, e.get("expansion"));
                return TypedDataComponent.create(StructureComponents.OUTER_BOX, new StructureOuterBoxComponent(
                    e.getOrDefaultObject(Boolean.class, "disable_block_break", false),
                    e.getOrDefaultObject(Boolean.class, "disable_block_place", false),
                    expansion
                ));
            }
        });
        reg.register("structure/store_blocks", new FileDataComponentType<StructureComponent>() {
            @Override
            public @Nullable TypedDataComponent<StructureComponent> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                return TypedDataComponent.create(StructureComponents.STORE_BLOCKS, new StructureStoredBlocksComponent(
                    e.getOrDefaultObject(Boolean.class, "disable_block_break", false),
                    e.getOrDefaultObject(Boolean.class, "disable_block_place", false)
                ));
            }
        });
        reg.register("structure/ticked_stored", new FileDataComponentType<StructureEditorComponent>() {
            @Override
            public @Nullable TypedDataComponent<StructureEditorComponent> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                return TypedDataComponent.create(StructureComponents.TICKED_STORED, new StructureTickedStoredComponent());
            }
        });
    }
}
