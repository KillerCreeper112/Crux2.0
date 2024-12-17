package killercreepr.cruxworlds.api.event;

import killercreepr.cruxworlds.api.world.creator.CruxWorldCreator;
import killercreepr.cruxworlds.api.world.creator.CruxWorldModuleCreator;
import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class CruxWorldPreCreateEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    protected final World world;
    protected final Collection<CruxWorldModuleCreator> moduleCreators;
    protected boolean cancel = false;

    public CruxWorldPreCreateEvent(World world, Collection<CruxWorldModuleCreator> moduleCreators) {
        this.world = world;
        this.moduleCreators = moduleCreators;
    }

    public World getWorld() {
        return world;
    }

    public Collection<CruxWorldModuleCreator> getModuleCreators() {
        return moduleCreators;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
