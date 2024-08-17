package killercreepr.crux.data.entity;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Predicate;

public class PlayerDataHolderRegistry extends DataHolderRegistry {
    public PlayerDataHolderRegistry(@NotNull Map<Key, DataHolder> map) {
        super(map);
    }

    public PlayerDataHolderRegistry() {
    }

    protected final @NotNull Map<Key, PlayerTickDataHolder> playerTickedHolders = new HashMap<>();

    @Override
    public <E extends DataHolder> void onRegistered(@NotNull Key key, @NotNull E value) {
        if(value instanceof PlayerTickDataHolder t){
            playerTickedHolders.put(key, t);
            return;
        }
        super.onRegistered(key, value);
    }

    @Override
    public void onRemoved(@NotNull Key key) {
        super.onRemoved(key);
        playerTickedHolders.remove(key);
    }

    @Override
    public void onRemoved(@NotNull Key key, @NotNull DataHolder value) {
        super.onRemoved(key, value);
        if(value instanceof PlayerTickDataHolder t) playerTickedHolders.remove(key, t);
    }

    public void removePlayerTickedIf(@NotNull Predicate<PlayerTickDataHolder> predicate){
        new HashSet<>(playerTickedHolders.values()).forEach(holder ->{
            if(predicate.test(holder)){
                unregister(holder);
            }
        });
    }
}
