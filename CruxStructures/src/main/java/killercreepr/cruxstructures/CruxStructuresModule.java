package killercreepr.cruxstructures;

import killercreepr.crux.module.CruxModule;
import killercreepr.crux.module.StandardModules;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxstructures.commands.StructureCommands;
import killercreepr.cruxstructures.config.*;
import killercreepr.cruxstructures.config.structure.FileBiomeRequirement;
import killercreepr.cruxstructures.config.structure.FileSurfaceCenter;
import killercreepr.cruxstructures.manager.StructureManager;
import killercreepr.cruxstructures.structure.StructureCenter;
import killercreepr.cruxstructures.structure.StructureRequirement;
import killercreepr.cruxstructures.structure.impl.CfgFAWEStructure;
import killercreepr.cruxstructures.structure.impl.CfgStructureGen;
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

    public FileStructureCenter getFileStructureCenter() {
        return fileStructureCenter;
    }

    public FileStructureRequirement getFileStructureRequirement() {
        return fileStructureRequirement;
    }

    public void registerCommands(@NotNull CruxPlugin plugin, @NotNull StructureManager structureManager){
        new StructureCommands(plugin, structureManager).register();
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        CfgRegistries.YAML.registerFileHandler(CfgStructureGen.class, new FileCfgStructureGen());
        CfgRegistries.YAML.registerFileHandler(StructureCenter.class, fileStructureCenter);
        CfgRegistries.YAML.registerFileHandler(StructureRequirement.class, fileStructureRequirement);

        CfgRegistries.YAML.registerFileHandler(CfgFAWEStructure.class, new FileCfgFAWEStructure());

        fileStructureCenter.TYPE_HANDLERS.register("surface_center", new FileSurfaceCenter());
        fileStructureRequirement.TYPE_HANDLERS.register("biome", new FileBiomeRequirement());

        CfgRegistries.JSON_REGISTRY.forEach(registry ->{
            registry.registerFileHandler(SimpleStoredStructure.class, new FileSimpleStoredStructure<SimpleStoredStructure>());
        });
    }
}
