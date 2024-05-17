package killercreepr.cruxconfig.config.bukkit.value;

import killercreepr.crux.data.NotNullHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class NotNullCfgValue<T> extends CfgValue<T> implements NotNullHolder<T> {
    public NotNullCfgValue(@NotNull ConfigValue<?> type, @NotNull String @Nullable ... comments) {
        super(type, comments);
    }

    public NotNullCfgValue(@Nullable String path, @NotNull ConfigValue<?> type, @NotNull String @Nullable ... comments) {
        super(path, type, comments);
    }

    public NotNullCfgValue(@Nullable T typeValue, @NotNull String @Nullable ... comments) {
        super(typeValue, comments);
    }

    public NotNullCfgValue(@Nullable String path, @Nullable T typeValue, @NotNull String @Nullable ... comments) {
        super(path, typeValue, comments);
    }

    @Override
    public @NotNull T value() {
        return Objects.requireNonNull(super.getOrDefault(cast(type.getDefaultValue(), null)));
    }

    @Override
    public NotNullCfgValue<T> setValue(@NotNull T value){
        super.setValue(Objects.requireNonNull(value));
        return this;
    }
}
