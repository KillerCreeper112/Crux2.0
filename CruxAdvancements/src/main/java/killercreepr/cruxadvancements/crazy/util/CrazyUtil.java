package killercreepr.cruxadvancements.crazy.util;

import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public class CrazyUtil {
    /*public static @NotNull Key toKey(@NotNull NameKey key){
        return Key.key(key.getNamespace(), key.getKey());
    }*/

    public static @NotNull NamespacedKey toNameKey(@NotNull Key key){
        return new NamespacedKey(key.namespace(), key.value());
    }
}
