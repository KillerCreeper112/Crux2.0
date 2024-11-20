package killercreepr.cruxconfig.config.common.base.registry;

import killercreepr.crux.core.registry.SimpleMappedRegistry;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class FileObjectHandlerRegistry extends SimpleMappedRegistry<Class<?>, FileObjectHandler<?>> {
    protected final @NotNull FileRegistry registry;

    public FileObjectHandlerRegistry(@NotNull Map<Class<?>, FileObjectHandler<?>> map, @NotNull FileRegistry registry) {
        super(map);
        this.registry = registry;
    }

    public FileObjectHandlerRegistry(@NotNull FileRegistry registry) {
        this.registry = registry;
    }

    @Override
    public <E extends FileObjectHandler<?>> @NotNull E register(@NotNull Class<?> key, @NotNull E value) {
        return super.register(key, value);
    }

    @Override
    public @Nullable FileObjectHandler<?> remove(@NotNull Class<?> key) {
        return super.remove(key);
    }

    @Override
    public boolean remove(@NotNull Class<?> key, @NotNull FileObjectHandler<?> value) {
        return super.remove(key, value);
    }

    public @NotNull FileRegistry getRegistry() {
        return registry;
    }
}
