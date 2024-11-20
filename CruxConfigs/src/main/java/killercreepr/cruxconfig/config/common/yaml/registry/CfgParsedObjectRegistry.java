package killercreepr.cruxconfig.config.common.yaml.registry;

import killercreepr.crux.core.registry.SimpleKeyedRegistry;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.parsed.CfgParsedObjectHandler;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * CfgParsedObjectHandlers provide external plugins the opportunity to change the outcome of an object that
 * has been deserialized from a YAML file.
 */
public class CfgParsedObjectRegistry extends SimpleKeyedRegistry<CfgParsedObjectHandler<?>> {
    public CfgParsedObjectRegistry(@NotNull Map<Key, CfgParsedObjectHandler<?>> map) {
        super(map);
    }

    public CfgParsedObjectRegistry() {
        super();
    }

    protected final @NotNull Map<Class<?>, TreeMap<Integer, Collection<CfgParsedObjectHandler<?>>>> SORTED = new HashMap<>();
    public <T> @Nullable TreeMap<Integer, Collection<CfgParsedObjectHandler<T>>> findFor(@NotNull Class<?> type){
        TreeMap<Integer, Collection<CfgParsedObjectHandler<?>>> found = SORTED.get(type);
        if(found==null){
            for(Map.Entry<Class<?>, TreeMap<Integer, Collection<CfgParsedObjectHandler<?>>>> entry : SORTED.entrySet()){
                if(entry.getKey().isAssignableFrom(type)){
                    return (TreeMap<Integer, Collection<CfgParsedObjectHandler<T>>>) (TreeMap<?, ?>) entry.getValue();
                }
            }
            return null;
        }
        return (TreeMap<Integer, Collection<CfgParsedObjectHandler<T>>>) (TreeMap<?, ?>) found;
    }

    public <T> @Nullable T parse(@NotNull YamlElement from, @NotNull YamlContext context, @NotNull T original){
        TreeMap<Integer, Collection<CfgParsedObjectHandler<T>>> map = findFor(original.getClass());
        if(map==null) return original;
        T current = original;
        for(Collection<CfgParsedObjectHandler<T>> list : map.values()){
            for(CfgParsedObjectHandler<T> handler : list){
                current = handler.parse(from, context, original, current);
            }
        }
        return current;
    }

    private void addSorted(@NotNull CfgParsedObjectHandler<?> handler){
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
    public <E extends CfgParsedObjectHandler<?>> @NotNull E register(@NotNull E object) {
        addSorted(object);
        return super.register(object);
    }

    @Override
    public <E extends CfgParsedObjectHandler<?>> @NotNull E register(@NotNull Key key, @NotNull E value) {
        addSorted(value);
        return super.register(key, value);
    }

    @Override
    public boolean unregister(@NotNull CfgParsedObjectHandler<?> object) {
        boolean x = super.unregister(object);
        if(x) removeSorted(object.key());
        return x;
    }

    @Override
    public @Nullable CfgParsedObjectHandler<?> remove(@NotNull Key key) {
        CfgParsedObjectHandler<?> removed = super.remove(key);
        if(removed != null) removeSorted(removed.key());
        return removed;
    }

    @Override
    public boolean remove(@NotNull Key key, @NotNull CfgParsedObjectHandler<?> value) {
        boolean x = super.remove(key, value);
        if(x) removeSorted(key);
        return x;
    }
}
