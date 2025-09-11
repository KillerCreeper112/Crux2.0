package killercreepr.crux.api.component;

import killercreepr.crux.api.data.Holder;
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
    static DataComponentHandler mergedAccessor(Collection<TypedDataComponent<?>> data, Holder<DataComponentAccessor> other){
        return new MergedAccessor(data, other);
    }
    static DataComponentHandler mergedAccessor(Holder<DataComponentAccessor> other){
        return new MergedAccessor(other);
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

        @Override
        public void clearComponents() {

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

    class MergedAccessor extends Simple{
        protected final Holder<DataComponentAccessor> other;

        public MergedAccessor(Holder<DataComponentAccessor> other) {
            this.other = other;
        }

        public MergedAccessor(Map<DataComponentType<?>, Holder<?>> map, Holder<DataComponentAccessor> other) {
            super(map);
            this.other = other;
        }

        public MergedAccessor(Collection<TypedDataComponent<?>> data, Holder<DataComponentAccessor> other) {
            super(data);
            this.other = other;
        }

        @Override
        public <T> @Nullable T get(DataComponentType<? extends T> type) {
            T value = super.get(type);
            if(value != null) return value;
            var other = this.other.value();
            if(other == null) return null;
            return other.get(type);
        }

        @Override
        public Set<DataComponentType<?>> keySet() {
            var other = this.other.value();
            if(other == null) return super.keySet();

            Set<DataComponentType<?>> copy = new HashSet<>(super.keySet());
            copy.addAll(other.keySet());
            return copy;
        }

        public Collection<TypedDataComponent<?>> buildTypedCollection(){
            Collection<TypedDataComponent<?>> list = super.buildTypedCollection();
            var other = this.other.value();
            if(other == null) return list;
            other.forEach(list::add);
            return list;
        }

        @NotNull
        @Override
        public Iterator<TypedDataComponent<?>> iterator() {
            return buildTypedCollection().iterator();
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
            if(value == null){
                holder = map.remove(type);
                if(type instanceof DataComponentType.Notify<? super T> notify){
                    notify.onComponentRemoved(
                        this, holder == null ? null : (T) holder.value()
                    );
                }
            }
            else{
                holder = map.put(type, Holder.direct(value));
                if(type instanceof DataComponentType.Notify<? super T> notify) notify.onComponentApplied(
                    this, value, holder == null ? null : (T) holder.value()
                );
            }
            return holder == null ? null : (T) holder.value();
        }

        @Override
        public void clearComponents() {
            map.clear();
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
