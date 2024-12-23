package killercreepr.cruxstructures.api.event;

import killercreepr.cruxstructures.api.structure.Structure;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class StructurePlaceEvent extends StructureEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    protected final @NotNull Location location;
    protected double rotation;
    protected boolean cancel = false;
    public StructurePlaceEvent(@NotNull Structure structure, @NotNull Location location, double rotation) {
        super(structure);
        this.location = location;
        this.rotation = rotation;
    }

    public StructurePlaceEvent(@NotNull Structure structure, boolean isAsync, @NotNull Location location, double rotation) {
        super(structure, isAsync);
        this.location = location;
        this.rotation = rotation;
    }

    public @NotNull Location getLocation() {
        return location;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
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

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
