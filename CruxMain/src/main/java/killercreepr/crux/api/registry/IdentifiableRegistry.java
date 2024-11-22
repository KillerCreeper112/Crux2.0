package killercreepr.crux.api.registry;

import killercreepr.crux.api.data.Identifiable;
import killercreepr.crux.api.data.StringIdentifiable;
import killercreepr.crux.core.registry.SimpleStringIdentifiableRegistry;
import org.jetbrains.annotations.NotNull;

public interface IdentifiableRegistry<I, T extends Identifiable<I>> extends MappedRegistry<I, T>{
    static <T extends StringIdentifiable> StringIdentifiableRegistry<T> stringIdentifiable(){
        return new SimpleStringIdentifiableRegistry<>();
    }
    @Override
    default <E extends T> @NotNull E register(@NotNull E object){
        return register(object.id(), object);
    }

    @Override
    default boolean unregister(@NotNull T object) {
        return remove(object.id()) != null;
    }
}

