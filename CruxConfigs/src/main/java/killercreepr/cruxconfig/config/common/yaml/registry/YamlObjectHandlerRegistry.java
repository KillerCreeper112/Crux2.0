package killercreepr.cruxconfig.config.common.yaml.registry;

import killercreepr.crux.registry.SimpleMappedRegistry;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class YamlObjectHandlerRegistry extends SimpleMappedRegistry<Class<?>, YamlObjectHandler<?>> {
    protected final @NotNull YamlRegistry registry;

    public YamlObjectHandlerRegistry(@NotNull Map<Class<?>, YamlObjectHandler<?>> map, @NotNull YamlRegistry registry) {
        super(map);
        this.registry = registry;
    }

    public YamlObjectHandlerRegistry(@NotNull YamlRegistry registry) {
        this.registry = registry;
    }

    @Override
    public @NotNull YamlObjectHandler<?> register(@NotNull Class<?> key, @NotNull YamlObjectHandler<?> value) {
        return super.register(key, value);
    }

    @Override
    public @Nullable YamlObjectHandler<?> remove(@NotNull Class<?> key) {
        YamlObjectHandler<?> removed = super.remove(key);
        return removed;
    }

    @Override
    public boolean remove(@NotNull Class<?> key, @NotNull YamlObjectHandler<?> value) {
        return super.remove(key, value);
    }

    public @NotNull YamlRegistry getRegistry() {
        return registry;
    }
}
