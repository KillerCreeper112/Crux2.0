package killercreepr.cruxmenus.menu.bukkit.slot;

import killercreepr.cruxmenus.menu.bukkit.Menu;
import org.jetbrains.annotations.NotNull;

public class SimpleTempStoredSlot extends SimpleSlot implements TempStoredSlot{
    public SimpleTempStoredSlot(@NotNull Menu menu, int index) {
        super(menu, index);
    }
}
