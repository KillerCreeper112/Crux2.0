package killercreepr.crux.core.key.tag;

import killercreepr.crux.api.key.tag.KeyTag;
import killercreepr.crux.api.key.tag.KeyTypeTag;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class SimpleKeyTag implements KeyTag, KeyTypeTag {
    protected final Key key;
    protected final Collection<Key> type;

    public SimpleKeyTag(Key key, Collection<Key> type) {
        this.key = key;
        this.type = type;
    }

    @Override
    public @NotNull Collection<Key> getTypes() {
        return type;
    }

    @Override
    public boolean isTagged(@NotNull Key item) {
        return type.contains(item);
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
