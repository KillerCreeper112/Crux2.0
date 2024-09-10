package killercreepr.cruxstructures.config.loader;

import killercreepr.crux.Crux;
import killercreepr.cruxconfig.config.bukkit.loader.CfgLoader;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import killercreepr.cruxstructures.config.FileCfgFAWEStructure;
import killercreepr.cruxstructures.registries.StructureRegistries;
import killercreepr.cruxstructures.structure.Structure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class StructureLoader extends CfgLoader {
    protected final @NotNull FileCfgFAWEStructure fileCfgFAWEStructure;
    public StructureLoader(@NotNull FileCfgFAWEStructure fileCfgFAWEStructure) {
        this.fileCfgFAWEStructure = fileCfgFAWEStructure;
    }

    @Override
    public void loadConfiguration(@NotNull DataFile cfg, @Nullable String path){
        Structure table;
        if(path == null) table = cfg.deserialize("", Structure.class);
        else{
            if(!(cfg.getRoot() instanceof FileObject root)) return;

            table = fileCfgFAWEStructure.deserializeFromFile(
                new FileContext<>(cfg.fileRegistry()), root, Crux.key(path)
            );
        }
        if(table == null) return;
        Crux.log(Level.INFO, "Registered structure: " + table.key());
        StructureRegistries.STRUCTURES.register(table);
    }
}
