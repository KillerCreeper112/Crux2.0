package killercreepr.cruxconfig.config.bukkit.data;

import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.value.ConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CommonValue<T> extends ConfigValue<T> {
    public CommonValue(@NotNull Class<T> type, @Nullable T defaultValue) {
        super(type, defaultValue);
    }

    public CommonValue(@NotNull Class<T> type) {
        super(type);
    }

    public void convertMap(@NotNull Map<?,?> map){

    }

    @Override
    public @Nullable T get(@NotNull CruxConfig cfg, @NotNull String path) {
        return cfg.deserialize(type, path);
    }

    @Override
    public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable T object) {
        cfg.set(path, object);
    }
}
