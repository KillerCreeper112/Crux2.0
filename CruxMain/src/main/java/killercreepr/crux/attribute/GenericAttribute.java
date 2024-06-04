package killercreepr.crux.attribute;

import killercreepr.crux.util.CruxString;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class GenericAttribute implements CruxAttribute{
    protected final @NotNull Key key;
    public GenericAttribute(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public @NotNull String getName() {
        return CruxString.toTitleCase(key.value());
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
