package killercreepr.cruxmenus.menu.bukkit.data;

import killercreepr.crux.data.DataExchange;
import killercreepr.cruxmenus.menu.bukkit.MenuContext;
import killercreepr.cruxmenus.menu.bukkit.MenuItem;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuItemHolder;
import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ItemDataParser extends Keyed {
    @NotNull
    DataExchange parse(@NotNull Player p, @NotNull MenuContext context, @NotNull MenuItemHolder item, @NotNull DataExchange info);

    default @NotNull DataExchange parse(@NotNull Player p, @NotNull MenuContext context, @NotNull MenuItemHolder item){
        return parse(p, context, item, item.info());
    }
}
