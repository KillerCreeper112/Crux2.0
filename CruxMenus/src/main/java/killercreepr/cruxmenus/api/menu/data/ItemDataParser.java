package killercreepr.cruxmenus.api.menu.data;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.cruxmenus.api.menu.contex.MenuContext;
import killercreepr.cruxmenus.api.menu.holder.MenuItemHolder;
import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ItemDataParser extends Keyed {
    @NotNull
    DataExchange parse(@NotNull Entity p, @NotNull MenuContext context, @NotNull MenuItemHolder item, @NotNull DataExchange info);

    default @NotNull DataExchange parse(@NotNull Entity p, @NotNull MenuContext context, @NotNull MenuItemHolder item){
        return parse(p, context, item, item.info());
    }
}
