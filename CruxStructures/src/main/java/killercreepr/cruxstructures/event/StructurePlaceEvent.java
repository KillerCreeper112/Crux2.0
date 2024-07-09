package killercreepr.cruxstructures.event;

import killercreepr.cruxstructures.structure.Structure;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class StructurePlaceEvent extends StructureEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    protected final @NotNull Location location;
    protected boolean cancel = false;
    public StructurePlaceEvent(@NotNull Structure structure, @NotNull Location location) {
        super(structure);
        this.location = location;
    }

    public @NotNull Location getLocation() {
        return location;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
