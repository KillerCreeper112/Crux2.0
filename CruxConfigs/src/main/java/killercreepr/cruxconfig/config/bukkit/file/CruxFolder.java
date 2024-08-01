package killercreepr.cruxconfig.config.bukkit.file;

import killercreepr.cruxconfig.config.common.file.ICruxFile;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class CruxFolder implements ICruxFile {
    public static @NotNull File file(@NotNull Plugin plugin, @NotNull String path){
        return new File(plugin.getDataFolder().getAbsolutePath() + "/" + path);
    }
    protected final @Nullable Plugin plugin;
    protected final File file;

    public CruxFolder(@NotNull Plugin plugin, @NotNull String path){
        this.plugin = plugin;
        file = file(plugin, path);
    }
    public CruxFolder(@NotNull File file){
        this.plugin = null;
        this.file = file;
    }

    @Override
    public final @NotNull File file(){
        return file;
    }

    public static @NotNull String withoutFileExtension(@NotNull String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return fileName; // No extension found
        } else {
            return fileName.substring(0, lastDotIndex);
        }
    }

    public static boolean hasFileExtension(@NotNull File file, @NotNull String extension){
        return file.getName().endsWith("." + extension);
    }
}
