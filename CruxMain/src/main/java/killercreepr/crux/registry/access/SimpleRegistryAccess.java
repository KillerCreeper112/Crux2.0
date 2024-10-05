package killercreepr.crux.registry.access;

import killercreepr.crux.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SimpleRegistryAccess implements RegistryAccess, RegistryRegistrar{
    protected final @NotNull Map<RegistryKey<?>, Registry<?>> map;

    public SimpleRegistryAccess() {
        this(new HashMap<>());
    }

    public SimpleRegistryAccess(@NotNull Map<RegistryKey<?>, Registry<?>> map) {
        this.map = map;
    }

    public <T extends Registry<?>> T registerRegistry(RegistryKey<T> key, T registry) {
        map.put(key, registry);
        return registry;
    }

    @Override
    public <T> Registry<T> getRegistry(RegistryKey<T> key) {
        return (Registry<T>) map.get(key);
    }
}
