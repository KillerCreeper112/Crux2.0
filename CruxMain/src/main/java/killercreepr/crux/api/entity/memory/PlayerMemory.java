package killercreepr.crux.api.entity.memory;

import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.crux.core.entity.memory.PlayerDataHolder;
import killercreepr.crux.core.entity.memory.SimplePlayerMemory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

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

    default <T extends DataHolder> T getPlayerDataHolderOrCompute(@NotNull Class<T> clazz, @NotNull Function<PlayerMemory, T> function){
        T holder = getDataHolder(clazz);
        if(holder==null){
            holder = function.apply(this);
            if(holder != null) getDataHolders().register(holder);
        }
        return holder;
    }

    @NotNull KeyedRegistry<PlayerDataHolder> getPlayerSpecificHolders();

    @Override
    @Nullable Player value();

    void scheduleForRemoval(@NotNull Player p);

    @Nullable Long quit();
}
