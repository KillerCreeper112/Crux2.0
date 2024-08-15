package killercreepr.cruxconfig.config.bukkit.file;

import killercreepr.cruxconfig.config.common.file.DataFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class BukkitDataFile {
    public static boolean isSupported(@NotNull File file){
        return CruxFolder.hasFileExtension(file, "yml") ||
            CruxFolder.hasFileExtension(file, "json");
    }
    public static @NotNull DataFile parse(@NotNull File file){
        if(CruxFolder.hasFileExtension(file, "yml")) return new CruxConfig(file);
        if(CruxFolder.hasFileExtension(file, "json")) return new CruxJson(file);
        throw new UnsupportedOperationException(file.getName() + " is not a supported file type!");
    }
}
