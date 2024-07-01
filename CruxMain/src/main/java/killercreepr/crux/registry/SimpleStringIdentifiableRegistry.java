package killercreepr.crux.registry;

import killercreepr.crux.data.StringIdentifiable;
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
    public @NotNull T register(@NotNull T object) {
        return StringIdentifiableRegistry.super.register(object);
    }

    @Override
    public boolean unregister(@NotNull T object) {
        return StringIdentifiableRegistry.super.unregister(object);
    }
}
