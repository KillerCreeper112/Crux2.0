package killercreepr.crux.data.entity;

import killercreepr.crux.registry.SimpleMappedRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class EntityMemoryRegistry<V extends EntityMemory> extends SimpleMappedRegistry<UUID, V> {
    @Override
    public <E extends V> @NotNull E register(@NotNull E object) {
        return register(object.getUUID(), object);
    }

    @Override
    public boolean unregister(@NotNull V object) {
        return remove(object.getUUID()) != null;
    }
}
