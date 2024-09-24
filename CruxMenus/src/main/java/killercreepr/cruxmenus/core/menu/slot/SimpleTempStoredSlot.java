package killercreepr.cruxmenus.core.menu.slot;

import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.slot.TempStoredSlot;
import org.jetbrains.annotations.NotNull;

public class SimpleTempStoredSlot extends SimpleSlot implements TempStoredSlot {
    public SimpleTempStoredSlot(@NotNull Menu menu, int index) {
        super(menu, index);
    }
}
