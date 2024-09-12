package killercreepr.crux.data.tag.block;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public abstract class BaseBlockTag implements BlockTag {
    protected final @NotNull Key key;
    public BaseBlockTag(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
