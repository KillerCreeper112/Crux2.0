package killercreepr.crux.core.registry;

import killercreepr.crux.api.data.StringIdentifiable;
import killercreepr.crux.api.registry.StringIdentifiableRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SimpleStringIdentifiableRegistry<T extends StringIdentifiable> extends SimpleMappedRegistry<String, T> implements StringIdentifiableRegistry<T> {
    public SimpleStringIdentifiableRegistry(@NotNull Map<String, T> map) {
        super(map);
    }
    public SimpleStringIdentifiableRegistry() {
        super();
    }

    @Override
    public <E extends T> @NotNull E register(@NotNull E object) {
        return StringIdentifiableRegistry.super.register(object);
    }

    @Override
    public boolean unregister(@NotNull T object) {
        return StringIdentifiableRegistry.super.unregister(object);
    }
}
