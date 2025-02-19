package killercreepr.cruxconfig.config.common.file;

import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public interface DataFile extends ICruxFile {
    @Nullable
    FileElement getRoot();
    @Nullable FileElement getElement(@NotNull String path);
    void serialize(@NotNull String path, @Nullable Object value);
    <T> @Nullable T deserialize(@NotNull String path, @NotNull Type type);
    <T> @Nullable T deserializeOrDefault(@NotNull String path, @NotNull Type type, @Nullable T fallback);
    @NotNull FileRegistry fileRegistry();
    char getPathSeparator();
    void setPathSeparator(char separator);
    void close();
    boolean save();
}
