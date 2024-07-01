package killercreepr.crux.data;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

//todo make this interface
public class DataExchange implements Iterable<Holder<Object>> {
    public static @NotNull DataExchange.Builder builder(){
        return new DataExchange.Builder();
    }

    public static @NotNull DataExchange empty(){ return new DataExchange(Maps.newHashMap()); }

    protected final @NotNull Map<String, Holder<Object>> data;
    public DataExchange(@NotNull Map<String, Holder<Object>> data){
        this.data = Collections.unmodifiableMap(data);
    }

    public DataExchange(@NotNull String id, @NotNull Holder<Object> holder){
        this(Map.of(id, holder));
    }

    /**
     * @return A new DataExchange with the appended info.
     */
    public @NotNull DataExchange append(@NotNull DataExchange info){
        Map<String, Holder<Object>> data = new HashMap<>(this.data);
        data.putAll(info.asMap());
        return new DataExchange(data);
    }

    /**
     * @return A new DataExchange with the appended object.
     */
    public @NotNull DataExchange append(@NotNull String id, @NotNull Holder<Object> object){
        Map<String, Holder<Object>> data = new HashMap<>(this.data);
        data.put(id, object);
        return new DataExchange(data);
    }

    /**
     * @return A new DataExchange with the removed values.
     */
    public @NotNull DataExchange removeIf(@NotNull Predicate<Object> predicate){
        Map<String, Holder<Object>> data = new HashMap<>(this.data);
        data.entrySet().removeIf((entry) -> predicate.test(entry.getKey(), entry.getValue()));
        return new DataExchange(data);
    }

    public boolean has(@NotNull String id){ return data.containsKey(id); }

    public <T> @NotNull Optional<T> getObject(@NotNull Class<T> findFirst){
        return Optional.ofNullable(get(findFirst));
    }

    public <T> @Nullable T get(@NotNull Class<T> findFirst){
        for(Holder<?> o : data.values()){
            if(o == null) continue;
            Object value = o.value();
            if(value==null) continue;
            if(findFirst.isAssignableFrom(value.getClass())) return findFirst.cast(value);
        }
        return null;
    }

    public <T> @NotNull Map<String, T> getObjects(@NotNull Class<T> findAll){
        Map<String, T> map = new HashMap<>();
        data.forEach((id, o) ->{
            if(o == null) return;
            Object value = o.value();
            if(value==null) return;
            if(findAll.isAssignableFrom(value.getClass())) map.put(id, findAll.cast(value));
        });
        return map;
    }

    public @NotNull Optional<Object> getObject(@NotNull String id){
        return Optional.ofNullable(get(id));
    }

    public <T> @NotNull Optional<T> getObject(@NotNull String id, @NotNull Class<T> find){
        return Optional.ofNullable(get(id, find));
    }

    public @Nullable Object get(@NotNull String id){
        Holder<?> holder = data.getOrDefault(id, null);
        return holder==null?null:holder.value();
    }

    public <T> @Nullable T get(@NotNull String id, @NotNull Class<T> find){
        Object found = get(id);
        if(found == null || !(find.isAssignableFrom(found.getClass()))) return null;
        return find.cast(found);
    }

    //Convenience methods.
    public <T> @NotNull T getOrThrow(@NotNull Class<T> find){
        T object = get(find);
        if(object==null) throw new IllegalArgumentException("Class, " + find.getSimpleName() + " is not present!");
        return object;
    }

    public @NotNull Object getOrThrow(@NotNull String id){
        Object object = get(id);
        if(object==null) throw new IllegalArgumentException(id + " is not present!");
        return object;
    }

    public <T> @NotNull T getOrThrow(@NotNull String id, @NotNull Class<T> find){
        T object = get(id, find);
        if(object==null) throw new IllegalArgumentException(id + " is not present!");
        return object;
    }

    /**
     * @return An immutable map containing this DataExchange's data.
     */
    public @NotNull Map<String, Holder<Object>> asMap() {
        return data;
    }

    @NotNull
    @Override
    public Iterator<Holder<Object>> iterator() {
        return data.values().iterator();
    }

    public interface Predicate<T>{
        boolean test(@NotNull String id, @NotNull Holder<T> holder);
    }

    public static class Builder{
        protected final Map<String, Holder<Object>> data = new HashMap<>();

        public Builder put(@NotNull String id, @NotNull Object direct){
            return put(id, Holder.direct(direct));
        }
        public Builder put(@NotNull Object direct){
            return put(direct.getClass().getSimpleName().toLowerCase(), Holder.direct(direct));
        }

        public Builder put(@NotNull String id, @NotNull Holder<Object> holder){
            data.put(id, holder);
            return this;
        }

        public Builder putAll(@Nullable DataExchange info){
            return info == null ? this : putAll(info.asMap());
        }

        public Builder putAll(@NotNull Map<String, Holder<Object>> map){
            data.putAll(map);
            return this;
        }

        public Builder remove(@NotNull String id){
            data.remove(id);
            return this;
        }

        public @NotNull DataExchange build(){
            return new DataExchange(data);
        }
    }
}
