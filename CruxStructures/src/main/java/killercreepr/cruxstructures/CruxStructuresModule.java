package killercreepr.cruxstructures;

import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.cruxconfig.config.bukkit.handler.impl.FileGenericEnum;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.FileSimpleLootTable;
import killercreepr.cruxconfig.config.bukkit.standard.CommonLootTableHandlers;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxstructures.commands.StructureCommands;
import killercreepr.cruxstructures.config.*;
import killercreepr.cruxstructures.config.generation.*;
import killercreepr.cruxstructures.config.location.FileDummyLocationFinder;
import killercreepr.cruxstructures.config.location.FileNearbySolidBlockFinder;
import killercreepr.cruxstructures.config.module.*;
import killercreepr.cruxstructures.location.LocationFinder;
import killercreepr.cruxstructures.manager.StructureManager;
import killercreepr.cruxstructures.structure.generation.StructureGenerator;
import killercreepr.cruxstructures.structure.generation.center.StructureCenter;
import killercreepr.cruxstructures.structure.generation.requirement.StructureChunkRequirement;
import killercreepr.cruxstructures.structure.generation.requirement.StructureRequirement;
import killercreepr.cruxstructures.structure.impl.CfgFAWEStructure;
import killercreepr.cruxstructures.structure.module.StructureModule;
import killercreepr.cruxstructures.structure.module.standard.WallsModule;
import killercreepr.cruxstructures.structure.stored.SimpleStoredStructure;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

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
        CfgRegistries.SIMPLE_REGISTRY.forEach(registry -> {
            registry.registerFileHandler(StructureGenerator.class, fileCfgStructureGen);
            registry.registerFileHandler(StructureCenter.class, fileStructureCenter);
            registry.registerFileHandler(StructureRequirement.class, fileStructureRequirement);
            registry.registerFileHandler(StructureChunkRequirement.class, fileStructureChunkRequirement);
            registry.registerFileHandler(StructureModule.class, fileStructureModule);

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

        CfgRegistries.JSON_REGISTRY.forEach(registry ->{
            registry.registerFileHandler(SimpleStoredStructure.class, new FileSimpleStoredStructure<SimpleStoredStructure>());
        });

        fileLocationFinder.TYPE_HANDLERS.register("nearby_solid_block", new FileNearbySolidBlockFinder());
        fileLocationFinder.TYPE_HANDLERS.register("dummy", new FileDummyLocationFinder());

        //CfgStructure
        fileCfgStructureGen.typeHandlers().register("set_location", new FileLocationSetStructureGen());
        fileCfgStructureGen.typeHandlers().register("instant_set_location", new FileInstantLocationSetStructureGen());
    }
}
