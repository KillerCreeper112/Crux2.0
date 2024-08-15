package killercreepr.cruxconfig.config.common.json.registry;

import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import killercreepr.cruxconfig.config.common.json.handler.JsonObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class JsonObjectHandlerRegistry extends SimpleMappedRegistry<Class<?>, JsonObjectHandler<?>> {
    protected final @NotNull TaggedJsonRegistry registry;

    public JsonObjectHandlerRegistry(@NotNull Map<Class<?>, JsonObjectHandler<?>> map, @NotNull TaggedJsonRegistry registry) {
        super(map);
        this.registry = registry;
    }

    public JsonObjectHandlerRegistry(@NotNull TaggedJsonRegistry registry) {
        this.registry = registry;
    }

    public final MappedRegistry<String, Class<?>> registryByName = SimpleMappedRegistry.fromHashMap();
    @Override
    public <E extends JsonObjectHandler<?>> @NotNull E register(@NotNull Class<?> key, @NotNull E value) {
        registryByName.register(registry.getSerializerID(value), key);
        return super.register(key, value);
    }

    @Override
    public @Nullable JsonObjectHandler<?> remove(@NotNull Class<?> key) {
        JsonObjectHandler<?> removed = super.remove(key);
        if(removed != null) registryByName.remove(registry.getSerializerID(removed));
        return removed;
    }

    @Override
    public boolean remove(@NotNull Class<?> key, @NotNull JsonObjectHandler<?> value) {
        registryByName.remove(registry.getSerializerID(value), key);
        return super.remove(key, value);
    }

    public @Nullable JsonObjectHandler<?> getByName(@NotNull String id){
        Class<?> found = registryByName.get(id);
        return found==null ? null : get(found);
    }

    public @NotNull TaggedJsonRegistry getRegistry() {
        return registry;
    }

    public MappedRegistry<String, Class<?>> getRegistryByName() {
        return registryByName;
    }
}
