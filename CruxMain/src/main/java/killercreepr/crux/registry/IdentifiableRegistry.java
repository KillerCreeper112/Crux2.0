package killercreepr.crux.registry;

import killercreepr.crux.data.Identifiable;
import org.jetbrains.annotations.NotNull;

public interface IdentifiableRegistry<T> extends MappedRegistry<T, Identifiable<T>>{
    @Override
    default @NotNull Identifiable<T> register(@NotNull Identifiable<T> object){
        return register(object.id(), object);
    }

    @Override
    default boolean unregister(@NotNull Identifiable<T> object) {
        return remove(object.id()) != null;
    }
}

