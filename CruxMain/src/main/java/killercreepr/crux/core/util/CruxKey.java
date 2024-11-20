package killercreepr.crux.core.util;

import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public class CruxKey {
    /**
     * Paper has been slowly moving towards using {@link net.kyori.adventure.key.Key}
     * This method is just a simple way to convert a {@link net.kyori.adventure.key.Key}
     * to a {@link NamespacedKey} because a ton of methods still require it.
     * But we're preparing for the future!
     */
    public static @NotNull NamespacedKey key(@NotNull Key key){
        return new NamespacedKey(key.namespace(), key.value());
    }
}
