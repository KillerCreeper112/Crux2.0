package killercreepr.sometests.menu;

import killercreepr.crux.data.DataExchange;
import killercreepr.cruxmenu.menu.bukkit.ConfigMenu;
import killercreepr.cruxmenu.menu.bukkit.holder.MenuHolder;
import killercreepr.crux.tags.container.ObjectStringHookContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BigBoi extends ConfigMenu {
    public BigBoi(@NotNull MenuHolder holder, @NotNull DataExchange info) {
        super(holder, info);
    }

    public BigBoi(@NotNull MenuHolder holder, @NotNull DataExchange info, @Nullable ObjectStringHookContainer tags) {
        super(holder, info, tags);
    }
}
