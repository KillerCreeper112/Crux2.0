package killercreepr.crux.data.tag.entity;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public abstract class BaseEntityTag implements EntityTag {
    protected final @NotNull Key key;
    public BaseEntityTag(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
