package killercreepr.crux.core.entity.tag;

import killercreepr.crux.core.Crux;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class SingleEntityTypeTag extends BaseEntityTag {
    protected final @NotNull Key value;
    public SingleEntityTypeTag(@NotNull Key key, @NotNull Key value) {
        super(key);
        this.value = value;
    }

    @Override
    public boolean isTagged(@NotNull Entity item) {
        return value.equals(Crux.handlers().entity().getType(item));
    }

    public @NotNull Key getType() {
        return value;
    }
}
