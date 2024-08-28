package killercreepr.crux;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public abstract class BaseItemTag implements ItemTag{
    protected final @NotNull Key key;
    public BaseItemTag(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
