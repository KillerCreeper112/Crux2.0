package killercreepr.crux.data.entity;

import killercreepr.crux.data.Holder;
import killercreepr.crux.registry.KeyedRegistry;
import killercreepr.crux.registry.SimpleKeyedRegistry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlayerMemory extends EntityMemory {
    protected long quit = -1;
    protected final KeyedRegistry<PlayerDataHolder> playerSpecificHolders = new SimpleKeyedRegistry<>();

    public @NotNull KeyedRegistry<PlayerDataHolder> getPlayerSpecificHolders() {
        return playerSpecificHolders;
    }

    protected final Holder<Player> entity;
    public PlayerMemory(@NotNull Player e) {
        this(e.getUniqueId(), Holder.weakReference(e));
    }

    public PlayerMemory(@NotNull UUID uuid, @NotNull Holder<Player> player){
        super(uuid, player);
        this.entity = player;
    }
    public static @Nullable PlayerMemory get(@NotNull Player p){
        return get(p.getUniqueId());
    }

    public static @NotNull PlayerMemory getOrCreate(@NotNull Player p){
        PlayerMemory d = get(p);
        if(d == null){
            d = register(new PlayerMemory(p));
        }
        return d;
    }


    public static @Nullable PlayerMemory get(@NotNull UUID uuid){
        if(EntityMemory.get(uuid) instanceof PlayerMemory d) return d;
        return null;
    }

    @Override
    public @Nullable Player value(){
        return entity.value();
    }

    public long quit(){ return quit; }

    public void quit(long time){
        quit = time;
    }

    @Override
    protected boolean shouldRemoveFromMemory(@Nullable Entity e) {
        return quit != -1L && System.currentTimeMillis() > quit;
    }

    @Override
    public boolean tick(){
        Player e = value();
        holders.values().removeIf(x ->{
            if(x.shouldRemoveFromMemory(e)){
                x.removing(e);
                return true;
            }
            if(e != null) x.tick(e);
            return false;
        });
        playerSpecificHolders.values().removeIf(x ->{
            if(x.shouldRemoveFromMemory(e)){
                x.removing(e);
                return true;
            }
            if(e != null) x.tick(e);
            return false;
        });
        if(shouldRemoveFromMemory(e)){
            holders.values().forEach(data -> data.parentRemoving(e));
            playerSpecificHolders.values().forEach(data -> data.parentRemoving(e));
            return true;
        }
        return false;
    }
}
