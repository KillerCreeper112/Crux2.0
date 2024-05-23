package killercreepr.cruxconfig.config.bukkit.value;

import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommonValue<T> extends CfgValue<T>{
    public CommonValue() {
    }

    public CommonValue(@Nullable T defaultValue) {
        super(defaultValue);
    }

    public CommonValue(@Nullable T defaultValue, @NotNull String @Nullable [] comments) {
        super(defaultValue, comments);
    }

    public CommonValue(@Nullable T defaultValue, @Nullable String path, @NotNull String @Nullable ... comments) {
        super(defaultValue, path, comments);
    }

    @Override
    public @Nullable T get(@NotNull CruxConfig cfg, @NotNull String path) {
        return (T) cfg.deserializeObject(parameterType, path);
    }

    @Override
    public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable T object) {
        cfg.set(path, object);
    }
}
