package killercreepr.cruxmenus.api.event.slot;

import killercreepr.cruxmenus.api.menu.slot.Slot;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class MenuSlotEvent extends Event implements Cancellable {
    protected boolean cancel = false;

    protected final @NotNull HumanEntity whoClicked;
    protected final @NotNull Slot slot;
    public MenuSlotEvent(@NotNull HumanEntity whoClicked, @NotNull Slot slot) {
        this.whoClicked = whoClicked;
        this.slot = slot;
    }

    public @NotNull HumanEntity getWhoClicked() {
        return whoClicked;
    }

    public @NotNull Slot getSlot() {
        return slot;
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
