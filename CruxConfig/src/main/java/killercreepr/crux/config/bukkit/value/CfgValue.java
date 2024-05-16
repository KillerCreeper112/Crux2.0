package killercreepr.crux.config.bukkit.value;

import killercreepr.crux.config.common.value.ICfgValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CfgValue<T> implements ICfgValue<T> {
    protected final @NotNull ConfigValue<?> type;
    protected final @Nullable String path;
    protected final @NotNull String @Nullable[] comments;

    public CfgValue(@NotNull ConfigValue<?> type, @NotNull String @Nullable... comments) {
        this(null, type, comments);
    }

    public CfgValue(@Nullable String path, @NotNull ConfigValue<?> type, @NotNull String @Nullable... comments) {
        this.type = type;
        this.path = path;
        this.comments = comments;
    }

    @Override
    public @NotNull ConfigValue<?> getType(){
        return type;
    }

    @Override
    public @Nullable String getPath(){
        return path;
    }

    @Override
    public @NotNull String @Nullable[] getComments(){
        return comments;
    }
}
