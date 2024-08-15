package killercreepr.cruxconfig.config.common.base.registry;

import killercreepr.crux.registry.SimpleKeyedRegistry;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.base.parsed.FileParsedObjectHandler;
import killercreepr.cruxconfig.config.common.element.FileElement;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * FileParsedObjectHandlers provide external plugins the opportunity to change the outcome of an object that
 * has been deserialized from a YAML file.
 */
public class FileParsedObjectRegistry extends SimpleKeyedRegistry<FileParsedObjectHandler<?>> {
    public FileParsedObjectRegistry(@NotNull Map<Key, FileParsedObjectHandler<?>> map) {
        super(map);
    }

    public FileParsedObjectRegistry() {
        super();
    }

    protected final @NotNull Map<Class<?>, TreeMap<Integer, Collection<FileParsedObjectHandler<?>>>> SORTED = new HashMap<>();
    public <T> @Nullable TreeMap<Integer, Collection<FileParsedObjectHandler<T>>> findFor(@NotNull Class<?> type){
        TreeMap<Integer, Collection<FileParsedObjectHandler<?>>> found = SORTED.get(type);
        if(found==null){
            for(Map.Entry<Class<?>, TreeMap<Integer, Collection<FileParsedObjectHandler<?>>>> entry : SORTED.entrySet()){
                if(entry.getKey().isAssignableFrom(type)){
                    return (TreeMap<Integer, Collection<FileParsedObjectHandler<T>>>) (TreeMap<?, ?>) entry.getValue();
                }
            }
            return null;
        }
        return (TreeMap<Integer, Collection<FileParsedObjectHandler<T>>>) (TreeMap<?, ?>) found;
    }

    public <T> @Nullable T parse(@NotNull FileElement from, @NotNull FileContext<?> context, @NotNull T original){
        TreeMap<Integer, Collection<FileParsedObjectHandler<T>>> map = findFor(original.getClass());
        if(map==null) return original;
        T current = original;
        for(Collection<FileParsedObjectHandler<T>> list : map.values()){
            for(FileParsedObjectHandler<T> handler : list){
                current = handler.parse(from, context, original, current);
            }
        }
        return current;
    }

    private void addSorted(@NotNull FileParsedObjectHandler<?> handler){
        removeSorted(handler.key());
        SORTED.computeIfAbsent(handler.getTargetType(),  i -> new TreeMap<>())
            .computeIfAbsent(handler.getPriority(), i -> new ArrayList<>()).add(handler);
    }

    private void removeSorted(@NotNull Key key){
        SORTED.values().removeIf(map ->{
            map.values().removeIf(list ->{
                if(list.removeIf(handler -> handler.key().equals(key))){
                    return list.isEmpty();
                }
                return list.isEmpty();
            });
            return map.isEmpty();
        });
    }

    @Override
    public <E extends FileParsedObjectHandler<?>> @NotNull E register(@NotNull E object) {
        addSorted(object);
        return super.register(object);
    }

    @Override
    public <E extends FileParsedObjectHandler<?>> @NotNull E register(@NotNull Key key, @NotNull E value) {
        addSorted(value);
        return super.register(key, value);
    }

    @Override
    public boolean unregister(@NotNull FileParsedObjectHandler<?> object) {
        boolean x = super.unregister(object);
        if(x) removeSorted(object.key());
        return x;
    }

    @Override
    public @Nullable FileParsedObjectHandler<?> remove(@NotNull Key key) {
        FileParsedObjectHandler<?> removed = super.remove(key);
        if(removed != null) removeSorted(removed.key());
        return removed;
    }

    @Override
    public boolean remove(@NotNull Key key, @NotNull FileParsedObjectHandler<?> value) {
        boolean x = super.remove(key, value);
        if(x) removeSorted(key);
        return x;
    }
}
