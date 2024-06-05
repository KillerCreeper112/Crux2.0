package killercreepr.cruxitems.registries;

import killercreepr.crux.registry.SimpleKeyedRegistry;
import killercreepr.cruxitems.item.CruxedItemUpdater;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class CruxedItemUpdaterRegistry extends SimpleKeyedRegistry<CruxedItemUpdater> {
    protected final TreeMap<Integer, Collection<CruxedItemUpdater>> BY_PRIORITY = new TreeMap<>();

    public @NotNull CruxedItemUpdater register(int priority, @NotNull CruxedItemUpdater object){
        register(object);
        BY_PRIORITY.computeIfAbsent(priority, (x) -> new ArrayList<>()).add(object);
        return object;
    }

    public boolean removeFromPriority(@NotNull Key key){
        for (Map.Entry<Integer, Collection<CruxedItemUpdater>> entry : BY_PRIORITY.entrySet()) {
            Collection<CruxedItemUpdater> keys = entry.getValue();
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

    public CruxedItemUpdaterRegistry forEachSorted(Consumer<? super CruxedItemUpdater> action){
        BY_PRIORITY.values().forEach(list -> list.forEach(action));
        return this;
    }

    @Override
    public boolean remove(@NotNull Key key, @NotNull CruxedItemUpdater value) {
        boolean x = super.remove(key, value);
        if(x){
            removeFromPriority(key);
        }
        return x;
    }

    @Override
    public @Nullable CruxedItemUpdater remove(@NotNull Key key) {
        CruxedItemUpdater removed = super.remove(key);
        if(removed != null){
            removeFromPriority(key);
        }
        return removed;
    }
}
