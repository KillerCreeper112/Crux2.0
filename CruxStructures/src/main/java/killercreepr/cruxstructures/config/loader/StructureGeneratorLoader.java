package killercreepr.cruxstructures.config.loader;

import killercreepr.cruxconfig.config.bukkit.loader.CfgLoader;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import killercreepr.cruxstructures.structure.generation.StructureGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class StructureGeneratorLoader extends CfgLoader {
    protected final @NotNull BiConsumer<DataFile, StructureGenerator> onLoad;
    public StructureGeneratorLoader(@NotNull BiConsumer<DataFile, StructureGenerator> onLoad) {
        this.onLoad = onLoad;
    }

    @Override
    public void loadConfiguration(@NotNull DataFile cfg, @Nullable String path){
        StructureGenerator table;
        if(path == null) table = cfg.deserialize("", StructureGenerator.class);
        else{
            if(!(cfg.getRoot() instanceof FileObject root)) return;

            table = cfg.deserialize("", StructureGenerator.class);
        }
        if(table == null) return;

        onLoad.accept(cfg, table);
    }
}
