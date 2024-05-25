package killercreepr.crux.registry;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SimpleKeyedRegistry<T extends Keyed> extends SimpleMappedRegistry<NamespacedKey, T> implements KeyedRegistry<T> {
    public SimpleKeyedRegistry(@NotNull Map<NamespacedKey, T> map) {
        super(map);
    }
    public SimpleKeyedRegistry() {
        super();
    }

    @Override
    public @NotNull T register(@NotNull T object){
        return register(object.getKey(), object);
    }

    @Override
    public boolean unregister(@NotNull T object) {
        return remove(object.getKey()) != null;
    }
}
