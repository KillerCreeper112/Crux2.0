package killercreepr.crux.api.key.tag;

import killercreepr.crux.api.data.tag.Tag;
import killercreepr.crux.core.key.tag.SimpleKeyTag;
import killercreepr.crux.core.key.tag.SingleKeyTag;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

public interface KeyTag extends Tag<Key> {
    static KeyTag keyTag(@NotNull Key tagType, @NotNull Key type){
        return new SingleKeyTag(tagType, type);
    }
    static KeyTag keyTag(@NotNull Key tagType, @NotNull Collection<Key> keys){
        return new SimpleKeyTag(tagType, keys);
    }
    static KeyTag keyTag(@NotNull Key tagType, @NotNull Key... keys){
        return keyTag(tagType, Arrays.asList(keys));
    }
}
