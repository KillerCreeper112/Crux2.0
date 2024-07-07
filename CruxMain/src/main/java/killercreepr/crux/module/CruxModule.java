package killercreepr.crux.module;

import killercreepr.crux.data.Reloadable;
import killercreepr.crux.plugin.CruxPlugin;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public interface CruxModule extends Reloadable {
    @NotNull String name();
    default void onEnable(@NotNull CruxPlugin plugin){}
    default void onDisable(@NotNull CruxPlugin plugin){}
    default void onLoad(@NotNull CruxPlugin plugin){}

    default @NotNull NamespacedKey key(@NotNull String key){
        return key(key.split(":"));
    }

    default @NotNull NamespacedKey key(@NotNull String[] key){
        return key.length > 1 ? new NamespacedKey(key[0], key[1]) : new NamespacedKey(name().toLowerCase(), key[0]);
    }

    default @NotNull String getKeyAsMinimalString(@NotNull NamespacedKey k){
        return keyHasNamespace(k) ? k.getKey() : k.asString();
    }

    default boolean keyHasNamespace(@NotNull NamespacedKey k){
        return k.getNamespace().equalsIgnoreCase(key("a").getNamespace());
    }
}
