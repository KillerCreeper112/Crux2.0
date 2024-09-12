package killercreepr.crux.data.tag;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public abstract class BaseTag<T> implements Tag<T>{
    protected final @NotNull Key key;
    public BaseTag(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
