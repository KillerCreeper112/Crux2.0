package killercreepr.cruxconfig.config.bukkit.loader;

import killercreepr.cruxconfig.config.bukkit.file.BukkitDataFile;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxconfig.config.common.file.DataFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public abstract class CfgLoader {
    public void loadConfiguration(@NotNull File folder){
        loadConfiguration(folder, null);
    }

    public void loadConfiguration(@NotNull File file, @Nullable String path){
        if(BukkitDataFile.isSupported(file)){
            loadConfiguration(
                BukkitDataFile.parse(file),
                path == null ? file.getName() : path + "/" + CruxFolder.withoutFileExtension(file.getName())
            );
            return;
        }
        File[] list = file.listFiles();
        if(list==null) return;
        for(File f : list){
            if(f.isDirectory()) path = path == null ? CruxFolder.withoutFileExtension(f.getName()) : (path + "/" + CruxFolder.withoutFileExtension(f.getName()));
            loadConfiguration(f, path);
        }
    }

    public abstract void loadConfiguration(@NotNull DataFile cfg, @Nullable String path);
}
