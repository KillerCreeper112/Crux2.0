package killercreepr.crux.data.entity;

import killercreepr.crux.registry.KeyedRegistry;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;

public interface PlayerMemory extends EntityMemory {
    static @Nullable PlayerMemory get(@NotNull Player p){
        return get(p.getUniqueId());
    }

    static @NotNull PlayerMemory getOrCreate(@NotNull Player p){
        return getOrCreate(p, null);
    }

    static @NotNull PlayerMemory getOrCreate(@NotNull Player p, @Nullable Consumer<PlayerMemory> newConsumer){
        PlayerMemory d = get(p);
        if(d == null){
            SimplePlayerMemory mem = new SimplePlayerMemory(p);
            if(newConsumer != null) newConsumer.accept(mem);
            d = EntityMemory.register(new SimplePlayerMemory(p));
        }
        return d;
    }

    static @Nullable PlayerMemory get(@NotNull UUID uuid){
        if(EntityMemory.get(uuid) instanceof PlayerMemory d) return d;
        return null;
    }

    @NotNull KeyedRegistry<PlayerDataHolder> getPlayerSpecificHolders();

    @Override
    @Nullable Player value();

    @Nullable Long quit();

    void quit(@Nullable Long time);
}
