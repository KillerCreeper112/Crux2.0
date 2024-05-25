package killercreepr.crux.registry;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a registered collection of mapped keyed objects. NamespacedKey -> KEYED_OBJECT
 */
public interface KeyedRegistry<T extends Keyed> extends MappedRegistry<NamespacedKey,T>{
    @Override
    default @NotNull T register(@NotNull T object){
        return register(object.getKey(), object);
    }

    @Override
    default boolean unregister(@NotNull T object) {
        return remove(object.getKey()) != null;
    }
}

