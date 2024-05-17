package killercreepr.cruxconfig.config.common.file;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ICruxConfig<T> extends ICruxFile {
    static @NotNull String addDot(@NotNull String s){
        return s.isBlank() ? s : s.endsWith(".") ? s : (s+".");
    }

    static @NotNull String removeDot(@NotNull String s){
        return s.endsWith(".") ? s.substring(0, s.length()-1) : s;
    }

    @NotNull T config();
    void set(@NotNull String path, @Nullable Object value);
    void set(@NotNull String path, @Nullable Object value, @NotNull String @Nullable... comments);
    void set(@NotNull String path, @Nullable Object value, @Nullable List<String> comments);
    void setComments(@NotNull String path, @NotNull String @Nullable... comments);
    @Nullable Object get(@NotNull String path);

    boolean contains(@NotNull String path);

    default void createDefaultAndRegister(){
        this.createDefault();
        this.register();
    }

    void register();
    boolean save();
    boolean createDefault();
}
