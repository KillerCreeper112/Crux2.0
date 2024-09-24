package killercreepr.cruxmenus.core.menu.slot;

import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.slot.FixedSlot;
import org.jetbrains.annotations.NotNull;

public class SimpleFixedSlot extends SimpleSlot implements FixedSlot {
    public SimpleFixedSlot(@NotNull Menu menu, int index) {
        super(menu, index);
    }
}
