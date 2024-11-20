package killercreepr.crux.api.component;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public interface DataComponentAccessor extends Iterable<TypedDataComponent<?>> {
    @Nullable
    <T> T get(DataComponentType<? extends T> type);

    Set<DataComponentType<?>> keySet();

    default <T> Collection<T> getAllOfType(Class<T> type){
        if(isEmpty()) return Set.of();
        Collection<T> list = new HashSet<>();
        for(TypedDataComponent<?> typed : this){
            Object o = typed.getValue();
            if(o == null) continue;
            if(type.isAssignableFrom(o.getClass())) list.add(type.cast(o));
        }
        return list;
    }

    default boolean has(DataComponentType<?> type) {
        return this.get(type) != null;
    }

    default <T> T getOrDefault(DataComponentType<? extends T> type, T defaultValue) {
        T object = this.get(type);
        return object != null ? object : defaultValue;
    }

    default int size() {
        return this.keySet().size();
    }

    default boolean isEmpty() {
        return this.size() == 0;
    }
}
