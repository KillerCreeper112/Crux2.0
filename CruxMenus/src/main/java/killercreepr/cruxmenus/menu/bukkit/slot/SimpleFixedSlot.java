package killercreepr.cruxmenus.menu.bukkit.slot;

import killercreepr.cruxmenus.menu.bukkit.Menu;
import org.jetbrains.annotations.NotNull;

public class SimpleFixedSlot extends SimpleSlot implements FixedSlot{
    public SimpleFixedSlot(@NotNull Menu menu, int index) {
        super(menu, index);
    }
}
