package killercreepr.crux.registry.access;

import killercreepr.crux.registry.Registry;

public interface RegistryAccess {
    <T> Registry<T> getRegistry(RegistryKey<T> key);
}
