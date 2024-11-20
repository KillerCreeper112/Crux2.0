package killercreepr.crux.core.entity.tag;

import killercreepr.crux.api.entity.tag.EntityTag;
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
