package killercreepr.crux.core.registry;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class IndirectKeyedRegistry<T> extends SimpleMappedRegistry<Key, T> {
    public IndirectKeyedRegistry(@NotNull Map<Key, T> map) {
        super(map);
    }
    public IndirectKeyedRegistry() {
        super();
    }

    @Override
    public <E extends T> @NotNull E register(@NotNull E object){
        return register(((Keyed) object).key(), object);
    }

    @Override
    public boolean unregister(@NotNull T object) {
        return remove(((Keyed) object).key()) != null;
    }
}
