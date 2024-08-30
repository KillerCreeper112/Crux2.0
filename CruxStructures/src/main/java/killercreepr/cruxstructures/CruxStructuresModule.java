package killercreepr.cruxstructures;

import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxstructures.commands.StructureCommands;
import killercreepr.cruxstructures.config.*;
import killercreepr.cruxstructures.config.generation.*;
import killercreepr.cruxstructures.config.module.FileCorruptedVeinModule;
import killercreepr.cruxstructures.manager.StructureManager;
import killercreepr.cruxstructures.structure.generation.center.StructureCenter;
import killercreepr.cruxstructures.structure.generation.requirement.StructureChunkRequirement;
import killercreepr.cruxstructures.structure.generation.requirement.StructureRequirement;
import killercreepr.cruxstructures.structure.impl.CfgFAWEStructure;
import killercreepr.cruxstructures.structure.impl.CfgStructureGen;
import killercreepr.cruxstructures.structure.module.StructureModule;
import killercreepr.cruxstructures.structure.stored.SimpleStoredStructure;
import org.jetbrains.annotations.NotNull;

public class CruxStructuresModule implements CruxModule {
    public static final String NAMESPACE = StandardModules.CRUX_STRUCTURES;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    protected final FileStructureCenter fileStructureCenter = new FileStructureCenter();
    protected final FileStructureRequirement fileStructureRequirement = new FileStructureRequirement();
    protected final FileStructureChunkRequirement fileStructureChunkRequirement = new FileStructureChunkRequirement();
    protected final FileStructureModule fileStructureModule = new FileStructureModule();

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
    public void onEnable(@NotNull CruxPlugin plugin) {
        CfgRegistries.SIMPLE_REGISTRY.forEach(registry -> {
            registry.registerFileHandler(CfgStructureGen.class, new FileCfgStructureGen());
            registry.registerFileHandler(StructureCenter.class, fileStructureCenter);
            registry.registerFileHandler(StructureRequirement.class, fileStructureRequirement);
            registry.registerFileHandler(StructureChunkRequirement.class, fileStructureChunkRequirement);
            registry.registerFileHandler(StructureModule.class, fileStructureModule);

            registry.registerFileHandler(CfgFAWEStructure.class, new FileCfgFAWEStructure());
        });

        fileStructureCenter.TYPE_HANDLERS.register("surface_center", new FileSurfaceCenter());

        fileStructureRequirement.TYPE_HANDLERS.register("biome", new FileBiomeRequirement());
        fileStructureRequirement.TYPE_HANDLERS.register("solid_nearby", new FileSolidNearbyRequirement());
        fileStructureRequirement.TYPE_HANDLERS.register("replaceable_nearby", new FileReplaceableNearbyRequirement());
        fileStructureRequirement.TYPE_HANDLERS.register("chance", new FileChanceRequirement());

        fileStructureChunkRequirement.TYPE_HANDLERS.register("noise", new FileChunkNoiseRequirement());

        fileStructureModule.TYPE_HANDLERS.register("corrupt_veins", new FileCorruptedVeinModule());

        CfgRegistries.JSON_REGISTRY.forEach(registry ->{
            registry.registerFileHandler(SimpleStoredStructure.class, new FileSimpleStoredStructure<SimpleStoredStructure>());
        });
    }
}
