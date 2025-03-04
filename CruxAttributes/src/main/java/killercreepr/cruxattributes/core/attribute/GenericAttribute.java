package killercreepr.cruxattributes.core.attribute;

import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class GenericAttribute implements CruxAttribute {
    protected final @NotNull Key key;
    public GenericAttribute(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public String toString() {
        return "GenericAttribute{key=" + key + "}";
    }

    @Override
    public int compareTo(@NotNull CruxAttribute o) {
        return key().compareTo(o.key());
    }
}
