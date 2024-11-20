package killercreepr.crux.api.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Identifiable<T> {
    @NotNull T id();
    default boolean compare(@Nullable Identifiable<T> other){
        return other != null && compare(other.id());
    }

    default boolean compare(@Nullable T id){
        return id().equals(id);
    }
}
