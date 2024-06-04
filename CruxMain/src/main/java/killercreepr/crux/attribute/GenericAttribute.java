package killercreepr.crux.attribute;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class GenericAttribute implements CruxAttribute{
    protected final @NotNull Key key;
    public GenericAttribute(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
