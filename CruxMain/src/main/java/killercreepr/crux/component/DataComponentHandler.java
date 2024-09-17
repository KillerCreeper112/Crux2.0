package killercreepr.crux.component;

import killercreepr.crux.data.Holder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public interface DataComponentHandler extends DataComponentAccessor, DataComponentEditor{
    static DataComponentHandler simple(){
        return new Simple();
    }

    static DataComponentHandler simple(Collection<TypedDataComponent<?>> data){
        return new Simple(data);
    }

    static DataComponentHandler empty(){
        return new Empty();
    }

    class Empty implements DataComponentHandler {
        @Override
        public <T> @Nullable T get(DataComponentType<? extends T> type) {
            return null;
        }

        @Override
        public Set<DataComponentType<?>> keySet() {
            return Set.of();
        }

        @Override
        public <T> @Nullable T set(DataComponentType<? super T> type, @Nullable T value) {
            return null;
        }

        /**
         * Returns an iterator over elements of type {@code T}.
         *
         * @return an Iterator.
         */
        @NotNull
        @Override
        public Iterator<TypedDataComponent<?>> iterator() {
            return Collections.emptyIterator();
        }
    }

    class Simple implements DataComponentHandler {
        protected final Map<DataComponentType<?>, Holder<?>> map;
        public Simple(){
            this(new HashMap<>());
        }
        public Simple(Map<DataComponentType<?>, Holder<?>> map) {
            this.map = map;
        }

        public Simple(Collection<TypedDataComponent<?>> data) {
            this();
            data.forEach(typed ->{
                map.put(typed.getType(), Holder.direct(typed.getValue()));
            });
        }

        @Override
        public <T> @Nullable T get(DataComponentType<? extends T> type) {
            Holder<?> holder = map.get(type);
            return holder == null ? null : (T) holder.value();
        }

        @Override
        public Set<DataComponentType<?>> keySet() {
            return map.keySet();
        }

        @Override
        public <T> @Nullable T set(DataComponentType<? super T> type, @Nullable T value) {
            Holder<?> holder;
            if(value == null) holder = map.remove(type);
            else holder = map.put(type, Holder.direct(value));
            return holder == null ? null : (T) holder.value();
        }

        public Collection<TypedDataComponent<?>> buildTypedCollection(){
            Collection<TypedDataComponent<?>> list = new HashSet<>();
            map.forEach((type, value) -> list.add(TypedDataComponent.createUnchecked(type, value.value())));
            return list;
        }

        @NotNull
        @Override
        public Iterator<TypedDataComponent<?>> iterator() {
            return buildTypedCollection().iterator();
        }
    }
}
