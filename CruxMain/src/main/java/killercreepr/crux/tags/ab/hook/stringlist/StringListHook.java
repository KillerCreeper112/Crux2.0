package killercreepr.crux.tags.ab.hook.stringlist;

import killercreepr.crux.tags.ab.hook.HookTag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class StringListHook<T> implements HookTag<T, List<String>> {
    protected final @NotNull Class<T> type;
    public StringListHook(@NotNull Class<T> type) {
        this.type = type;
    }

    @Override
    public @NotNull Class<T> getObjectType() {
        return type;
    }
}
