package killercreepr.cruxmenus.menu.bukkit.slot;

import killercreepr.cruxmenus.menu.bukkit.Menu;
import org.jetbrains.annotations.NotNull;

public class SimpleSlot implements Slot{
    protected final @NotNull Menu menu;
    protected final int index;

    public SimpleSlot(@NotNull Menu menu, int index) {
        this.menu = menu;
        this.index = index;
    }

    @Override
    public @NotNull Menu getMenu() {
        return menu;
    }

    @Override
    public int getIndex() {
        return index;
    }
}
