package killercreepr.crux.api.component;

import com.google.common.collect.ImmutableMap;
import killercreepr.crux.api.data.Holder;
import killercreepr.crux.core.data.util.MapBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public interface DataComponentAccessor extends Iterable<TypedDataComponent<?>> {
    static DataComponentAccessor simple(Collection<TypedDataComponent<?>> types){
        return new SimpleImmutable(types);
    }
    static DataComponentAccessor mergedAccessor(Collection<TypedDataComponent<?>> data, Holder<DataComponentAccessor> other){
        return new MergedAccessor(data, other);
    }

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
    default <T> boolean hasAnyOfType(Class<T> type){
        if(isEmpty()) return false;
        for(TypedDataComponent<?> typed : this){
            Object o = typed.getValue();
            if(o == null) continue;
            if(type.isAssignableFrom(o.getClass())) return true;
        }
        return false;
    }

    default <T> void forEachAllOfType(Class<T> type, Consumer<T> consumer){
        if(isEmpty()) return;
        for(TypedDataComponent<?> typed : this){
            Object o = typed.getValue();
            if(o == null) continue;
            if(type.isAssignableFrom(o.getClass())) consumer.accept(type.cast(o));
        }
    }

    default boolean has(DataComponentType<?> type){
        return get(type) != null;
    }

    default <T> T getOrDefault(DataComponentType<? extends T> type, T defaultValue) {
        T object = this.get(type);
        return object != null ? object : defaultValue;
    }
    default <T> T getOrThrow(DataComponentType<? extends T> type){
        T object = this.get(type);
        Objects.requireNonNull(object, "DataComponentAccessor " + type + " is not present!");
        return object;
    }

    default int size() {
        return this.keySet().size();
    }

    default boolean isEmpty() {
        return this.size() == 0;
    }

    interface ImmutableHandler extends DataComponentAccessor{
        @Contract(pure = true)
        <T> ImmutableHandler with(DataComponentType<? extends T> type, T value);
        @Contract(pure = true)
        <T> ImmutableHandler with(DataComponentType<? extends T> type, Holder<T> value);
    }

    class SimpleImmutableHandler extends SimpleImmutable implements ImmutableHandler{

        public SimpleImmutableHandler(Map<DataComponentType<?>, Holder<?>> map) {
            super(map);
        }

        public SimpleImmutableHandler(Collection<TypedDataComponent<?>> data) {
            super(data);
        }

        @Override
        public <T> ImmutableHandler with(DataComponentType<? extends T> type, T value) {
            return with(type, Holder.direct(value));
        }

        @Override
        public <T> ImmutableHandler with(DataComponentType<? extends T> type, Holder<T> value) {
            return new SimpleImmutableHandler(addAndCopy(type, value));
        }

        protected <T> Map<DataComponentType<?>, Holder<?>> addAndCopy(DataComponentType<? extends T> type, T value){
            return addAndCopy(type, Holder.direct(value));
        }

        protected <T> Map<DataComponentType<?>, Holder<?>> addAndCopy(DataComponentType<? extends T> type, Holder<T> value){
            Map<DataComponentType<?>, Holder<?>> map = new HashMap<>(this.map);
            if(value == null) map.remove(type);
            else map.put(type, value);
            return ImmutableMap.copyOf(map);
        }

        @Override
        public String toString() {
            return "SimpleImmutableHandler{" +
                "map=" + map +
                '}';
        }
    }

    class MergedAccessor extends DataComponentAccessor.SimpleImmutable {
        protected final Holder<DataComponentAccessor> other;

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

    class SimpleImmutable implements DataComponentAccessor {
        protected final Map<DataComponentType<?>, Holder<?>> map;
        public SimpleImmutable(Map<DataComponentType<?>, Holder<?>> map) {
            this.map = ImmutableMap.copyOf(map);
        }

        public SimpleImmutable(Collection<TypedDataComponent<?>> data) {
            this(new MapBuilder<DataComponentType<?>, Holder<?>>()
                .apply(builder ->{
                    data.forEach(typed ->{
                        builder.put(typed.getType(), Holder.direct(typed.getValue()));
                    });
                })
                .buildUnmodifiable());
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

        @Override
        public String toString() {
            return "SimpleImmutable{" +
                "map=" + map +
                '}';
        }
    }
}
