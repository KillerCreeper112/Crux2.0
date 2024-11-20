package killercreepr.crux.api.registry.access;

import killercreepr.crux.api.registry.Registry;

public interface RegistryAccess {
    <T> Registry<T> getRegistry(RegistryKey<T> key);
}
