package killercreepr.cruxconfig.config.common.file;

import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxconfig.config.bukkit.file.CruxJson;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Type;

public interface DataFile extends ICruxFile{
    //todo not a good look
    static boolean isSupported(@NotNull File file){
        return CruxFolder.hasFileExtension(file, "yml") ||
            CruxFolder.hasFileExtension(file, "json");
    }
    static @NotNull DataFile parse(@NotNull File file){
        if(CruxFolder.hasFileExtension(file, "yml")) return new CruxConfig(file);
        if(CruxFolder.hasFileExtension(file, "json")) return new CruxJson(file, CfgRegistries.FILE);
        throw new UnsupportedOperationException(file.getName() + " is not a supported file type!");
    }
    @Nullable
    FileElement getRoot();
    @Nullable FileElement getElement(@NotNull String path);
    void serialize(@NotNull String path, @Nullable Object value);
    <T> @Nullable T deserialize(@NotNull String path, @NotNull Type type);
    @NotNull FileRegistry fileRegistry();
    char getPathSeparator();
    void setPathSeparator(char separator);
}
