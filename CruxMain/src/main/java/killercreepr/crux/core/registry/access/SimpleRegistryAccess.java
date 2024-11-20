package killercreepr.crux.core.registry.access;

import killercreepr.crux.api.registry.Registry;
import killercreepr.crux.api.registry.access.RegistryAccess;
import killercreepr.crux.api.registry.access.RegistryKey;
import killercreepr.crux.api.registry.access.RegistryRegistrar;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SimpleRegistryAccess implements RegistryAccess, RegistryRegistrar {
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
