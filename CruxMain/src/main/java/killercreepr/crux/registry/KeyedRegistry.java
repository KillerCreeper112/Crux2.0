package killercreepr.crux.registry;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a registered collection of mapped keyed objects. NamespacedKey -> KEYED_OBJECT
 */
public interface KeyedRegistry<T extends Keyed> extends MappedRegistry<Key,T>{
    @Override
    default @NotNull T register(@NotNull T object){
        return register(object.key(), object);
    }

    @Override
    default boolean unregister(@NotNull T object) {
        return remove(object.key()) != null;
    }
}

