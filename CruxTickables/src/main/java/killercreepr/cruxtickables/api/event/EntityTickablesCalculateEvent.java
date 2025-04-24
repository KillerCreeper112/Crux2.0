package killercreepr.cruxtickables.api.event;

import killercreepr.crux.core.Crux;
import killercreepr.cruxtickables.api.entity.tickable.ActiveEntityTickable;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class EntityTickablesCalculateEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    protected final Entity entity;
    protected final Map<Key, ActiveEntityTickable> tickables;

    public EntityTickablesCalculateEvent(Entity entity, Map<Key, ActiveEntityTickable> tickables) {
        super(!Crux.isPrimaryThread());
        this.entity = entity;
        this.tickables = tickables;
    }

    public boolean hasTickable(Key key){
        return tickables.containsKey(key);
    }

    public void addTickable(ActiveEntityTickable tickable){
        tickables.put(tickable.getTickable().key(), tickable);
    }

    public ActiveEntityTickable removeTickable(Key key){
        return tickables.remove(key);
    }

    public Entity getEntity() {
        return entity;
    }

    public Map<Key, ActiveEntityTickable> getTickables() {
        return tickables;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
