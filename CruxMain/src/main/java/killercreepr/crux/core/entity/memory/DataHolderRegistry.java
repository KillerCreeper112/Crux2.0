package killercreepr.crux.core.entity.memory;

import killercreepr.crux.api.entity.memory.DataHolder;
import killercreepr.crux.api.entity.memory.TickedDataHolder;
import killercreepr.crux.core.registry.SimpleKeyedRegistry;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class DataHolderRegistry extends SimpleKeyedRegistry<DataHolder> {
    public DataHolderRegistry(@NotNull Map<Key, DataHolder> map) {
        super(map);
    }

    public DataHolderRegistry() {
    }

    protected final @NotNull Map<Key, TickedDataHolder> tickedHolders = new ConcurrentHashMap<>();

    public <E extends DataHolder> void onRegistered(@NotNull Key key, @NotNull E value){
        if(value instanceof TickedDataHolder t){
            tickedHolders.put(key, t);
        }
        value.adding();
    }

    public void onRemoved(@NotNull Key key){
        tickedHolders.remove(key);
    }

    public void onRemoved(@NotNull Key key, @NotNull DataHolder value){
        if(value instanceof TickedDataHolder t) tickedHolders.remove(key, t);
    }

    @Override
    public <E extends DataHolder> @NotNull E register(@NotNull Key key, @NotNull E value) {
        var out = super.register(key, value);
        onRegistered(key, value);
        return out;
    }

    @Override
    public @Nullable DataHolder remove(@NotNull Key key) {
        var out = super.remove(key);
        onRemoved(key);
        return out;
    }

    @Override
    public boolean remove(@NotNull Key key, @NotNull DataHolder value) {
        var out = super.remove(key, value);
        onRemoved(key, value);
        return out;
    }

    public @NotNull Map<Key, TickedDataHolder> getTickedHolders() {
        return tickedHolders;
    }

    /*public void removeTickedIf(@NotNull Predicate<TickedDataHolder> predicate){
        tickedHolders.values().removeIf(holder ->{
            if(predicate.test(holder)){
                //unregister(holder);
                map.remove(holder.key());
                return true;
            }
            return false;
        });
    }*/
}
