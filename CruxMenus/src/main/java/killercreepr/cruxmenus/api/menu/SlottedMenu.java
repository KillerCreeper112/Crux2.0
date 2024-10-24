package killercreepr.cruxmenus.api.menu;

import killercreepr.cruxmenus.api.menu.slot.Slot;
import org.jetbrains.annotations.NotNull;

public interface SlottedMenu extends Menu {
    void addSlot(@NotNull Slot slot);
    void addSlot(@NotNull Slot slot, boolean skipUpdate);
}
