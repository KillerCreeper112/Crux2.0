package killercreepr.crux.component;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public interface DataComponentAccessor extends Iterable<DataComponentType<?>> {
    @Nullable
    <T> T get(DataComponentType<? extends T> type);

    Set<DataComponentType<?>> keySet();

    default <T> Collection<T> getAllOfType(Class<T> type){
        Collection<T> list = new HashSet<>();
        for(DataComponentType<?> componentType : this){
            Object o = get(componentType);
            if(o == null) continue;
            if(type.isAssignableFrom(o.getClass())) list.add(type.cast(o));
        }
        return list;
    }

    @NotNull
    @Override
    default Iterator<DataComponentType<?>> iterator(){
        return keySet().iterator();
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
