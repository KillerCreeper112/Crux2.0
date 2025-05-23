package killercreepr.cruxmenus.api.menu.holder;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.data.holder.DataInfoHolder;
import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.cruxmenus.api.menu.action.click.ClickActions;
import killercreepr.cruxmenus.api.menu.contex.MenuContext;
import killercreepr.cruxmenus.api.menu.item.MenuItem;
import killercreepr.cruxmenus.core.menu.holder.SimpleMenuItemHolder;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MenuItemHolder extends DataInfoHolder {
    static MenuItemHolder holder(@NotNull Holder<DynamicItem> item, @NotNull DataExchange info,
                                 @Nullable ClickActions clickActions){
        return new SimpleMenuItemHolder(item, info, clickActions);
    }
    @NotNull
    MenuItem getDisplayItem(@NotNull Entity p, @NotNull MenuContext info);

    @NotNull Holder<DynamicItem> getItem();

    @Nullable
    ClickActions getClickActions();

    interface InfoEvaluator{
        @NotNull
        MenuContext evaluateInfo();
    }
}
