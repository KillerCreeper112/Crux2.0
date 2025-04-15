package killercreepr.cruxworlds.api.event;

import killercreepr.crux.core.Crux;
import killercreepr.cruxworlds.api.world.CruxWorld;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CruxWorldDeleteEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    protected final CruxWorld world;
    /**
     * The default constructor is defined for cleaner code. This constructor
     * assumes the event is synchronous.
     */
    public CruxWorldDeleteEvent(CruxWorld world) {
        super(!Crux.isPrimaryThread());
        this.world = world;
    }

    public CruxWorld getWorld() {
        return world;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
