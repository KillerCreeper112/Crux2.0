package killercreepr.crux.data.entity;

import killercreepr.crux.registry.SimpleMappedRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class EntityMemoryRegistry<V extends EntityMemory> extends SimpleMappedRegistry<UUID, V> {
    @Override
    public @NotNull V register(@NotNull V object) {
        return register(object.getUUID(), object);
    }

    @Override
    public boolean unregister(@NotNull V object) {
        return remove(object.getUUID()) != null;
    }
}
