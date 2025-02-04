package killercreepr.cruxconfig.config.bukkit.value;

import killercreepr.cruxconfig.config.common.file.DataFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public class CommonValue<T> extends CfgValue<T>{
    public CommonValue() {
        super();
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

    public CommonValue(@Nullable T defaultValue, @Nullable String path, @NotNull Type parameterType, @NotNull String @Nullable ... comments) {
        super(defaultValue, path, parameterType, comments);
    }

    @Override
    public @Nullable T get(@NotNull DataFile cfg, @NotNull String path) {
        return (T) cfg.deserialize(path, parameterType);
    }

    @Override
    public void set(@NotNull DataFile cfg, @NotNull String path, @Nullable T object) {
        cfg.serialize(path, object);
    }
}
