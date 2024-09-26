package killercreepr.cruxconfig.config.bukkit.value;

import com.google.common.base.Preconditions;
import killercreepr.crux.data.Holder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public class NotNullValue<T> extends CommonValue<T> implements Holder<T> {
    public NotNullValue(@NotNull T defaultValue) {
        super(Preconditions.checkNotNull(defaultValue));
    }

    public NotNullValue(@NotNull T defaultValue, @NotNull String @Nullable [] comments) {
        super(Preconditions.checkNotNull(defaultValue), comments);
    }

    public NotNullValue(@NotNull T defaultValue, @Nullable String path, @NotNull String @Nullable ... comments) {
        super(Preconditions.checkNotNull(defaultValue), path, comments);
    }

    public NotNullValue(@NotNull T defaultValue, @Nullable String path, @NotNull Type parameterType, @NotNull String @Nullable ... comments) {
        super(Preconditions.checkNotNull(defaultValue), path, parameterType, comments);
    }

    @Override
    public @NotNull T value() {
        T value = getValue();
        return Preconditions.checkNotNull(value == null ? defaultValue : value);
    }
}
