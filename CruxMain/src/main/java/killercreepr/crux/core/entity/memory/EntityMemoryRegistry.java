package killercreepr.crux.core.entity.memory;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.core.registry.SimpleMappedRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EntityMemoryRegistry<V extends EntityMemory> extends SimpleMappedRegistry<UUID, V> {
    public EntityMemoryRegistry(@NotNull Map<UUID, V> map) {
        super(map);
    }

    /**
     * Creates a registry using a HashMap.
     */
    public EntityMemoryRegistry() {
        this(new ConcurrentHashMap<>());
    }

    @Override
    public <E extends V> @NotNull E register(@NotNull E object) {
        return register(object.getUUID(), object);
    }

    @Override
    public boolean unregister(@NotNull V object) {
        return remove(object.getUUID()) != null;
    }
}
