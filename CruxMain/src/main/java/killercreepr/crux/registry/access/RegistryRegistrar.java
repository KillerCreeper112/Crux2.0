package killercreepr.crux.registry.access;

import killercreepr.crux.registry.Registry;

public interface RegistryRegistrar {
    <T extends Registry<?>> T registerRegistry(RegistryKey<T> key, T registry);
    <T> Registry<T> getRegistry(RegistryKey<T> key);
}
