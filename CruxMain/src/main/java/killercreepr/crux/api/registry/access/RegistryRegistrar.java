package killercreepr.crux.api.registry.access;

import killercreepr.crux.api.registry.Registry;

public interface RegistryRegistrar {
    <T extends Registry<?>> T registerRegistry(RegistryKey<T> key, T registry);
    <T> Registry<T> getRegistry(RegistryKey<T> key);
}
