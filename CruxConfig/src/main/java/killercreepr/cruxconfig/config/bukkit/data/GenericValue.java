package killercreepr.cruxconfig.config.bukkit.data;

import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.value.ConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GenericValue extends ConfigValue<Object> {

    public GenericValue(@Nullable Object defaultValue) {
        super(Object.class, defaultValue);
    }

    public GenericValue() {
        super(Object.class);
    }

    @Nullable
    @Override
    public Object get(@NotNull CruxConfig cfg, @NotNull String path) {
        return cfg.get(path);
    }

    @Override
    public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable Object object) {
        cfg.set(path, object);
    }
}
