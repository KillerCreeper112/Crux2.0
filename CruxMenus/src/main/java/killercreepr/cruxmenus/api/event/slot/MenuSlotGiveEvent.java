package killercreepr.cruxmenus.api.event.slot;

import killercreepr.cruxmenus.api.menu.slot.Slot;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MenuSlotGiveEvent extends MenuSlotEvent{
    private static final HandlerList HANDLER_LIST = new HandlerList();
    protected int amount;
    public MenuSlotGiveEvent(@NotNull HumanEntity whoClicked, @NotNull Slot slot, int amount) {
        super(whoClicked, slot);
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
