package killercreepr.crux.core.data;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.data.Holder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;

public class SimpleDataExchange implements DataExchange {
    protected final @NotNull Map<String, Holder<?>> data;
    protected final @Nullable Set<String> notExplictSet;

    public SimpleDataExchange(@NotNull Map<String, Holder<?>> data, @Nullable Set<String> notExplictSet) {
        this.data = Collections.unmodifiableMap(data);
        this.notExplictSet = notExplictSet == null || notExplictSet.isEmpty() ? null : Collections.unmodifiableSet(notExplictSet);
    }

    public SimpleDataExchange(@NotNull String id, @NotNull Holder<?> holder, @Nullable Set<String> notExplictSet) {
        this(Map.of(id, holder), notExplictSet);
    }

    @Override
    public boolean isExplicitlySet(@NotNull String id) {
        return notExplictSet == null || !notExplictSet.contains(id);
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    /**
     * @return A new DataExchange with the appended info.
     */
    @Contract(pure = true)
    public @NotNull DataExchange append(@NotNull DataExchange info) {
        if(info instanceof SimpleDataExchange d && d.notExplictSet != null){
            Set<String> explicit = new HashSet<>();
            if(this.notExplictSet != null) explicit.addAll(this.notExplictSet);
            explicit.addAll(d.notExplictSet);
            Map<String, Holder<?>> data = new HashMap<>(this.data);
            data.putAll(info.asMap());
            return new SimpleDataExchange(data, explicit);
        }
        return append(info.asMap());
    }

    /**
     * @return A new DataExchange with the appended info.
     */
    @Contract(pure = true)
    public @NotNull DataExchange append(@NotNull Map<String, Holder<?>> info) {
        Map<String, Holder<?>> data = new HashMap<>(this.data);
        data.putAll(info);
        return new SimpleDataExchange(data, notExplictSet);
    }

    /**
     * @return A new DataExchange with the appended info.
     */
    @Override
    public @NotNull DataExchange appendObjects(@NotNull Map<String, ?> info) {
        Map<String, Holder<?>> data = new HashMap<>(this.data);
        info.forEach((id, value) ->{
            data.put(id, Holder.direct(value));
        });
        return new SimpleDataExchange(data, notExplictSet);
    }

    /**
     * @return A new DataExchange with the appended object.
     */
    @Contract(pure = true)
    public @NotNull DataExchange append(@NotNull String id, @NotNull Holder<?> object) {
        Map<String, Holder<?>> data = new HashMap<>(this.data);
        data.put(id, object);
        return new SimpleDataExchange(data, notExplictSet);
    }

    /**
     * @return A new DataExchange with the removed values.
     */
    @Contract(pure = true)
    public @NotNull DataExchange removeIf(@NotNull DataExchange.Predicate predicate) {
        Map<String, Holder<?>> data = new HashMap<>(this.data);
        Set<String> explicit = notExplictSet == null ? null : new HashSet<>(notExplictSet);
        data.entrySet().removeIf((entry) ->{
            if(predicate.test(entry.getKey(), entry.getValue())){
                if(explicit != null) explicit.remove(entry.getKey());
                return true;
            }
            return false;
        });
        return new SimpleDataExchange(data, explicit);
    }

    /**
     * @return A new DataExchange with the removed values.
     */
    @Override
    public @NotNull DataExchange remove(@NotNull String... ids) {
        Map<String, Holder<?>> data = new HashMap<>(this.data);
        Set<String> explicit = notExplictSet == null ? null : new HashSet<>(notExplictSet);
        for(String i : ids){
            data.remove(i);
            if(explicit != null) explicit.remove(i);
        }
        return new SimpleDataExchange(data, explicit);
    }

    public boolean has(@NotNull String id) {
        return data.containsKey(id);
    }

    public <T> @NotNull Optional<T> getObject(@NotNull Class<T> findFirst) {
        return Optional.ofNullable(get(findFirst));
    }

    public <T> @Nullable T get(@NotNull Class<T> findFirst) {
        T attempt = get(findFirst.getSimpleName().toLowerCase(), findFirst);
        if (attempt != null) return attempt;

        for (Holder<?> o : data.values()) {
            if (o == null) continue;
            Object value = o.value();
            if (value == null) continue;
            if (findFirst.isAssignableFrom(value.getClass())) return findFirst.cast(value);
        }
        return null;
    }

    public <T> @NotNull Map<String, T> getObjects(@NotNull Class<T> findAll) {
        Map<String, T> map = new HashMap<>();
        data.forEach((id, o) -> {
            if (o == null) return;
            Object value = o.value();
            if (value == null) return;
            if (findAll.isAssignableFrom(value.getClass())) map.put(id, findAll.cast(value));
        });
        return map;
    }

    public @NotNull Optional<Object> getObject(@NotNull String id) {
        return Optional.ofNullable(get(id));
    }

    public <T> @NotNull Optional<T> getObject(@NotNull String id, @NotNull Class<T> find) {
        return Optional.ofNullable(get(id, find));
    }

    public @Nullable Object get(@NotNull String id) {
        Holder<?> holder = data.getOrDefault(id, null);
        return holder == null ? null : holder.value();
    }

    public <T> @Nullable T get(@NotNull String id, @NotNull Class<T> find) {
        Object found = get(id);
        if (found == null || !(find.isAssignableFrom(found.getClass()))) return null;
        return find.cast(found);
    }

    @Override
    public DataExchange forEach(BiConsumer<String, Holder<?>> consumer) {
        data.forEach(consumer);
        return this;
    }

    //Convenience methods.
    public <T> @NotNull T getOrThrow(@NotNull Class<T> find) {
        T object = get(find);
        if (object == null) throw new IllegalArgumentException("Class, " + find.getSimpleName() + " is not present!");
        return object;
    }

    public @NotNull Object getOrThrow(@NotNull String id) {
        Object object = get(id);
        if (object == null) throw new IllegalArgumentException(id + " is not present!");
        return object;
    }

    public <T> @NotNull T getOrThrow(@NotNull String id, @NotNull Class<T> find) {
        T object = get(id, find);
        if (object == null) throw new IllegalArgumentException(id + " is not present!");
        return object;
    }

    public <T> T getOrDefault(@NotNull Class<T> find, @Nullable T defaultValue) {
        T found = get(find);
        return found == null ? defaultValue : found;
    }

    public Object getOrDefault(@NotNull String id, @Nullable Object defaultValue) {
        Object found = get(id);
        return found == null ? defaultValue : found;
    }

    public <T> T getOrDefault(@NotNull String id, @NotNull Class<T> type, @Nullable T defaultValue) {
        T found = get(id, type);
        return found == null ? defaultValue : found;
    }

    /**
     * @return An immutable map containing this DataExchange's data.
     */
    public @NotNull Map<String, Holder<?>> asMap() {
        return data;
    }

    @NotNull
    @Override
    public Iterator<Holder<?>> iterator() {
        return data.values().iterator();
    }

    public interface Predicate {
        boolean test(@NotNull String id, @NotNull Holder<?> holder);
    }

    public static class Builder implements DataExchange.Builder {
        protected final Map<String, Holder<?>> data = new HashMap<>();
        protected final Set<String> notExplictSet = new HashSet<>();

        @Override
        public <T> T getOrDefault(Class<T> type, T fallback) {
            for(Holder<?> holder : data.values()){
                Object o = holder.value();
                if(o == null) continue;
                if(!type.isAssignableFrom(o.getClass())) continue;
                return type.cast(o);
            }
            return fallback;
        }

        public DataExchange.Builder putAll(@Nullable Object direct, @NotNull String... ids) {
            return putAll(Holder.direct(direct), ids);
        }

        public DataExchange.Builder put(@NotNull String id, @Nullable Object direct) {
            return put(id, Holder.direct(direct));
        }

        public DataExchange.Builder put(@NotNull Object direct) {
            String id = direct.getClass().getSimpleName().toLowerCase();
            notExplictSet.add(id);
            return put(id, Holder.direct(direct));
        }

        public DataExchange.Builder putAll(@NotNull Holder<?> holder, @NotNull String... ids) {
            for (String i : ids) {
                put(i, holder);
            }
            return this;
        }

        public DataExchange.Builder put(@NotNull String id, @NotNull Holder<?> holder) {
            data.put(id, holder);
            return this;
        }

        public DataExchange.Builder putAll(@Nullable DataExchange info) {
            if(info instanceof SimpleDataExchange d && d.notExplictSet != null){
                this.notExplictSet.addAll(d.notExplictSet);
            }
            return info == null ? this : putAll(info.asMap());
        }

        public DataExchange.Builder putAll(@NotNull Map<String, Holder<?>> map) {
            data.putAll(map);
            return this;
        }

        public DataExchange.Builder remove(@NotNull String id) {
            data.remove(id);
            return this;
        }

        public @NotNull DataExchange build() {
            if(data.isEmpty() && notExplictSet.isEmpty()) return DataExchange.empty();
            return new SimpleDataExchange(data, notExplictSet);
        }
    }
}
