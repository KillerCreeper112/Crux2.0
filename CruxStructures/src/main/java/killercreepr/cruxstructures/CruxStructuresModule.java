package killercreepr.cruxstructures;

import killercreepr.crux.module.CruxModule;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxstructures.config.FileCfgStructureGen;
import killercreepr.cruxstructures.config.FileSimpleStoredStructure;
import killercreepr.cruxstructures.config.FileStructureCenter;
import killercreepr.cruxstructures.config.FileStructureRequirement;
import killercreepr.cruxstructures.config.structure.FileBiomeRequirement;
import killercreepr.cruxstructures.config.structure.FileSurfaceCenter;
import killercreepr.cruxstructures.structure.StructureCenter;
import killercreepr.cruxstructures.structure.StructureRequirement;
import killercreepr.cruxstructures.structure.impl.CfgStructureGen;
import killercreepr.cruxstructures.structure.stored.SimpleStoredStructure;
import org.jetbrains.annotations.NotNull;

public class CruxStructuresModule implements CruxModule {
    public static final String NAMESPACE = "CruxStructures";
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

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        CfgRegistries.YAML.registerHandler(CfgStructureGen.class, new FileCfgStructureGen());
        CfgRegistries.YAML.registerHandler(StructureCenter.class, fileStructureCenter);
        CfgRegistries.YAML.registerHandler(StructureRequirement.class, fileStructureRequirement);

        fileStructureCenter.TYPE_HANDLERS.register("surface_center", new FileSurfaceCenter());
        fileStructureRequirement.TYPE_HANDLERS.register("biome", new FileBiomeRequirement());

        CfgRegistries.JSON.registerHandler(SimpleStoredStructure.class, new FileSimpleStoredStructure());
    }
}
