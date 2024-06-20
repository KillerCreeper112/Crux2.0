package killercreepr.crux.tags.ab.hook.string;

import killercreepr.crux.tags.ab.hook.HookTag;
import org.jetbrains.annotations.NotNull;

public abstract class StringHook<T> implements HookTag<T, String> {
    protected final @NotNull Class<T> type;
    public StringHook(@NotNull Class<T> type) {
        this.type = type;
    }

    @Override
    public @NotNull Class<T> getObjectType() {
        return type;
    }
}
