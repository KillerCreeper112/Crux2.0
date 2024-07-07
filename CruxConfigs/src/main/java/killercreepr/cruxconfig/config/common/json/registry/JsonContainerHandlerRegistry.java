package killercreepr.cruxconfig.config.common.json.registry;

import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import killercreepr.cruxconfig.config.common.json.JsonRegistry;
import killercreepr.cruxconfig.config.common.json.container.JsonContainerHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class JsonContainerHandlerRegistry extends SimpleMappedRegistry<Class<?>, JsonContainerHandler<?>> {
    protected final @NotNull JsonRegistry registry;

    public JsonContainerHandlerRegistry(@NotNull Map<Class<?>, JsonContainerHandler<?>> map, @NotNull JsonRegistry registry) {
        super(map);
        this.registry = registry;
    }

    public JsonContainerHandlerRegistry(@NotNull JsonRegistry registry) {
        this.registry = registry;
    }

    public final MappedRegistry<String, Class<?>> registryByName = SimpleMappedRegistry.fromHashMap();
    @Override
    public <E extends JsonContainerHandler<?>> @NotNull E register(@NotNull Class<?> key, @NotNull E value) {
        registryByName.register(registry.getSerializerID(value), key);
        return super.register(key, value);
    }

    @Override
    public @Nullable JsonContainerHandler<?> remove(@NotNull Class<?> key) {
        JsonContainerHandler<?> removed = super.remove(key);
        if(removed != null) registryByName.remove(registry.getSerializerID(removed));
        return removed;
    }

    @Override
    public boolean remove(@NotNull Class<?> key, @NotNull JsonContainerHandler<?> value) {
        registryByName.remove(registry.getSerializerID(value), key);
        return super.remove(key, value);
    }

    public @Nullable JsonContainerHandler<?> getByName(@NotNull String id){
        Class<?> found = registryByName.get(id);
        return found==null ? null : get(found);
    }

    public @NotNull JsonRegistry getRegistry() {
        return registry;
    }

    public MappedRegistry<String, Class<?>> getRegistryByName() {
        return registryByName;
    }
}
