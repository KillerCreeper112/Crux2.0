package killercreepr.crux.tags.hook;

import org.jetbrains.annotations.NotNull;

public abstract class ObjectHook<T> {
    protected final @NotNull Class<T> object;
    public abstract @NotNull String identifier();

    public ObjectHook(@NotNull Class<T> object) {
        this.object = object;
    }

    public @NotNull Class<T> getObject() {
        return object;
    }
}
