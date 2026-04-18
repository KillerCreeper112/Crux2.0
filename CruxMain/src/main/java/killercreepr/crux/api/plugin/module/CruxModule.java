package killercreepr.crux.api.plugin.module;

import killercreepr.crux.api.data.Reloadable;
import killercreepr.crux.core.plugin.CruxPlugin;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public interface CruxModule extends Reloadable {
    @NotNull String name();
    default void onLoad(@NotNull CruxPlugin plugin){}
    default void onEnable(@NotNull CruxPlugin plugin){}
    default void onDisable(@NotNull CruxPlugin plugin){}
    default @NotNull NamespacedKey key(@NotNull String key) {
        int colon = key.indexOf(':');
        return colon == -1
          ? new NamespacedKey(name().toLowerCase(), key)
          : new NamespacedKey(key.substring(0, colon), key.substring(colon + 1));
    }

    default @NotNull NamespacedKey key(@NotNull String[] key) {
        return key.length > 1 ? new NamespacedKey(key[0], key[1]) : new NamespacedKey(name().toLowerCase(), key[0]);
    }

    default @NotNull String getKeyAsMinimalString(@NotNull NamespacedKey k){
        return keyHasNamespace(k) ? k.getKey() : k.asString();
    }

    default boolean keyHasNamespace(@NotNull NamespacedKey k){
        return k.getNamespace().equalsIgnoreCase(key("a").getNamespace());
    }
}
