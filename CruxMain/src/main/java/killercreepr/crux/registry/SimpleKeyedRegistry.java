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
}
