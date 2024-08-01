package killercreepr.cruxadvancements.crazy;

import eu.endercentral.crazy_advancements.NameKey;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class CrazyUtil {
    public static @NotNull Key toKey(@NotNull NameKey key){
        return Key.key(key.getNamespace(), key.getKey());
    }

    public static @NotNull NameKey toNameKey(@NotNull Key key){
        return new NameKey(key.namespace(), key.value());
    }
}
