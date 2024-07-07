package killercreepr.crux.registry;

import killercreepr.crux.data.Identifiable;
import org.jetbrains.annotations.NotNull;

public interface IdentifiableRegistry<I, T extends Identifiable<I>> extends MappedRegistry<I, T>{
    @Override
    default <E extends T> @NotNull E register(@NotNull E object){
        return register(object.id(), object);
    }

    @Override
    default boolean unregister(@NotNull T object) {
        return remove(object.id()) != null;
    }
}

