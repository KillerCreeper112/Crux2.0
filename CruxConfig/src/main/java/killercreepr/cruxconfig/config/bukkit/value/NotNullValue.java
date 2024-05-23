package killercreepr.cruxconfig.config.bukkit.value;

import com.google.common.base.Preconditions;
import killercreepr.crux.data.NotNullHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NotNullValue<T> extends CommonValue<T> implements NotNullHolder<T> {
    public NotNullValue(@NotNull T defaultValue) {
        super(Preconditions.checkNotNull(defaultValue));
    }

    public NotNullValue(@NotNull T defaultValue, @NotNull String @Nullable [] comments) {
        super(Preconditions.checkNotNull(defaultValue), comments);
    }

    public NotNullValue(@NotNull T defaultValue, @Nullable String path, @NotNull String @Nullable ... comments) {
        super(Preconditions.checkNotNull(defaultValue), path, comments);
    }

    @Override
    public @NotNull T value() {
        return Preconditions.checkNotNull(getOrDefaultValue());
    }
}
