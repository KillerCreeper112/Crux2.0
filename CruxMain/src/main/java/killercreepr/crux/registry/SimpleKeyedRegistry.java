package killercreepr.crux.registry;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SimpleKeyedRegistry<T extends Keyed> extends SimpleMappedRegistry<Key, T> implements KeyedRegistry<T> {
    public SimpleKeyedRegistry(@NotNull Map<Key, T> map) {
        super(map);
    }
    public SimpleKeyedRegistry() {
        super();
    }

    @Override
    public @NotNull T register(@NotNull T object){
        return register(object.key(), object);
    }

    @Override
    public boolean unregister(@NotNull T object) {
        return remove(object.key()) != null;
    }
}
