package killercreepr.crux.data.entity;

import killercreepr.crux.registry.SimpleKeyedRegistry;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class DataHolderRegistry extends SimpleKeyedRegistry<DataHolder> {
    public DataHolderRegistry(@NotNull Map<Key, DataHolder> map) {
        super(map);
    }

    public DataHolderRegistry() {
    }

    protected final @NotNull Map<Key, TickedDataHolder> tickedHolders = new HashMap<>();

    public <E extends DataHolder> void onRegistered(@NotNull Key key, @NotNull E value){
        if(value instanceof TickedDataHolder t) tickedHolders.put(key, t);
    }

    public void onRemoved(@NotNull Key key){
        tickedHolders.remove(key);
    }

    public void onRemoved(@NotNull Key key, @NotNull DataHolder value){
        if(value instanceof TickedDataHolder t) tickedHolders.remove(key, t);
    }

    @Override
    public <E extends DataHolder> @NotNull E register(@NotNull Key key, @NotNull E value) {
        onRegistered(key, value);
        return super.register(key, value);
    }

    @Override
    public @Nullable DataHolder remove(@NotNull Key key) {
        onRemoved(key);
        return super.remove(key);
    }

    @Override
    public boolean remove(@NotNull Key key, @NotNull DataHolder value) {
        onRemoved(key, value);
        return super.remove(key, value);
    }

    public @NotNull Map<Key, TickedDataHolder> getTickedHolders() {
        return tickedHolders;
    }

    public void removeTickedIf(@NotNull Predicate<TickedDataHolder> predicate){
        tickedHolders.values().removeIf(holder ->{
            if(predicate.test(holder)){
                //unregister(holder);
                map.remove(holder.key());
                return true;
            }
            return false;
        });
    }
}
