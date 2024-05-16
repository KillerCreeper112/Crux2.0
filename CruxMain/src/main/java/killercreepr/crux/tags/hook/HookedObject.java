package killercreepr.crux.tags.hook;

import killercreepr.crux.data.Holder;
import killercreepr.crux.tags.Tags;
import org.jetbrains.annotations.NotNull;

public interface HookedObject<T, E extends ObjectHook<T>> {
    @NotNull
    Tags tags();
    @NotNull String identifier();

    @NotNull String getPrefix();

    @NotNull
    Holder<T> getHolder();

    @NotNull E getHook();
}
