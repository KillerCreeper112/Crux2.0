package killercreepr.crux.registry;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class KeyedPriorityRegistry<T extends Keyed> extends SimpleKeyedRegistry<T> implements PriorityRegistry<T> {
    protected final TreeMap<Integer, Collection<T>> BY_PRIORITY = new TreeMap<>();

    @Override
    public @NotNull T register(int priority, @NotNull T object){
        register(object);
        BY_PRIORITY.computeIfAbsent(priority, (x) -> new ArrayList<>()).add(object);
        return object;
    }
    @Override
    public boolean removeFromPriority(@NotNull Key key){
        for (Map.Entry<Integer, Collection<T>> entry : BY_PRIORITY.entrySet()) {
            Collection<T> keys = entry.getValue();
            if (keys != null) {
                keys.removeIf(x -> x.key().equals(key));
                if (keys.isEmpty()) {
                    BY_PRIORITY.remove(entry.getKey());
                }
                return true;
            }
        }
        return false;
    }
    @Override
    public KeyedPriorityRegistry<T> forEachSorted(Consumer<? super T> action){
        BY_PRIORITY.values().forEach(list -> list.forEach(action));
        return this;
    }

    @Override
    public @NotNull List<T> buildSortedList(){
        List<T> list = new ArrayList<>();
        forEachSorted(list::add);
        return list;
    }

    @Override
    public boolean remove(@NotNull Key key, @NotNull T value) {
        boolean x = super.remove(key, value);
        if(x){
            removeFromPriority(key);
        }
        return x;
    }

    @Override
    public @Nullable T remove(@NotNull Key key) {
        T removed = super.remove(key);
        if(removed != null){
            removeFromPriority(key);
        }
        return removed;
    }

    public TreeMap<Integer, Collection<T>> getByPriority() {
        return BY_PRIORITY;
    }
}
