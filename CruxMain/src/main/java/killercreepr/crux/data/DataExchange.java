package killercreepr.crux.data;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DataExchange {
    public static @NotNull DataExchange empty(){ return new DataExchange(Maps.newHashMap()); }

    protected final Map<String, Holder<Object>> data;
    public DataExchange(@NotNull Map<String, Holder<Object>> data){
        this.data = data;
    }

    public DataExchange(@NotNull String id, @NotNull Holder<Object> holder){
        this.data = new HashMap<>();
        data.put(id, holder);
    }

    public @NotNull DataExchange append(@NotNull DataExchange info){
        Map<String, Holder<Object>> data = new HashMap<>(this.data);
        data.putAll(info.getData());
        return new DataExchange(data);
    }

    public @NotNull DataExchange append(@NotNull String id, @NotNull Holder<Object> object){
        Map<String, Holder<Object>> data = new HashMap<>(this.data);
        data.put(id, object);
        return new DataExchange(data);
    }

    public @NotNull DataExchange removeIf(@NotNull Predicate<Object> predicate){
        Map<String, Holder<Object>> data = new HashMap<>(this.data);
        data.entrySet().removeIf((entry) -> predicate.test(entry.getKey(), entry.getValue()));
        return new DataExchange(data);
    }

    public boolean has(@NotNull String id){ return data.containsKey(id); }

    public <T> @NotNull Optional<T> getObject(@NotNull Class<T> findFirst){
        for(Holder<?> o : data.values()){
            if(o == null) continue;
            Object value = o.value();
            if(value==null) continue;
            if(findFirst.isAssignableFrom(value.getClass())) return Optional.of(findFirst.cast(value));
        }
        return Optional.empty();
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
        Holder<?> holder = data.getOrDefault(id, null);
        return Optional.ofNullable(holder==null?null:holder.value());
    }

    public <T> @NotNull Optional<T> getObject(@NotNull String id, @NotNull Class<T> find){
        Object found = getObject(id).orElse(null);
        if(found == null || !(find.isAssignableFrom(found.getClass()))) return Optional.empty();
        return Optional.of(find.cast(found));
    }

    //Convenience methods.
    public @NotNull Object getObjectOrThrow(@NotNull String id){
        return getObject(id).orElseThrow(() -> new RuntimeException(id + " is not present!"));
    }

    public <T> @NotNull T getObjectOrThrow(@NotNull String id, @NotNull Class<T> find){
        return getObject(id, find).orElseThrow(() -> new RuntimeException(id + " is not present!"));
    }

    public @NotNull Map<String, Holder<Object>> getData() {
        return data;
    }

    public interface Predicate<T>{
        boolean test(@NotNull String id, @NotNull Holder<T> holder);
    }

    public static class Builder{
        protected final Map<String, Holder<Object>> data = new HashMap<>();

        public Builder put(@NotNull String id, @NotNull Holder<Object> holder){
            data.put(id, holder);
            return this;
        }

        public Builder putAll(@Nullable DataExchange info){
            return info == null ? this : putAll(info.getData());
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
