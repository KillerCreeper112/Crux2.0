package killercreepr.cruxconfig.config.common.yaml.registry;

import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import killercreepr.cruxconfig.config.common.yaml.YamlRegistry;
import killercreepr.cruxconfig.config.common.yaml.container.YamlContainerHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class YamlContainerHandlerRegistry extends SimpleMappedRegistry<Class<?>, YamlContainerHandler<?>> {
    protected final @NotNull YamlRegistry registry;

    public YamlContainerHandlerRegistry(@NotNull Map<Class<?>, YamlContainerHandler<?>> map, @NotNull YamlRegistry registry) {
        super(map);
        this.registry = registry;
    }

    public YamlContainerHandlerRegistry(@NotNull YamlRegistry registry) {
        this.registry = registry;
    }

    public final MappedRegistry<String, Class<?>> registryByName = SimpleMappedRegistry.fromHashMap();
    @Override
    public @NotNull YamlContainerHandler<?> register(@NotNull Class<?> key, @NotNull YamlContainerHandler<?> value) {
        registryByName.register(registry.getSerializerID(value), key);
        return super.register(key, value);
    }

    @Override
    public @Nullable YamlContainerHandler<?> remove(@NotNull Class<?> key) {
        YamlContainerHandler<?> removed = super.remove(key);
        if(removed != null) registryByName.remove(registry.getSerializerID(removed));
        return removed;
    }

    @Override
    public boolean remove(@NotNull Class<?> key, @NotNull YamlContainerHandler<?> value) {
        registryByName.remove(registry.getSerializerID(value), key);
        return super.remove(key, value);
    }

    public @Nullable YamlContainerHandler<?> getByName(@NotNull String id){
        Class<?> found = registryByName.get(id);
        return found==null ? null : get(found);
    }

    public @NotNull YamlRegistry getRegistry() {
        return registry;
    }

    public MappedRegistry<String, Class<?>> getRegistryByName() {
        return registryByName;
    }
}
