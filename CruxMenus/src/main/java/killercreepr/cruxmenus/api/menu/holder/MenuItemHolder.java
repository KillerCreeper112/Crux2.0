package killercreepr.cruxmenus.api.menu.holder;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.data.holder.DataInfoHolder;
import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.api.item.dynamic.MergeOption;
import killercreepr.cruxmenus.api.menu.action.click.ClickActions;
import killercreepr.cruxmenus.api.menu.contex.MenuContext;
import killercreepr.cruxmenus.api.menu.item.MenuItem;
import killercreepr.cruxmenus.core.menu.holder.SimpleMenuItemHolder;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface MenuItemHolder extends DataInfoHolder {
    static MenuItemHolder holder(@NotNull Holder<DynamicItem> item, @NotNull DataExchange info,
                                 @Nullable ClickActions clickActions,
                                 Map<String, MergeOption> itemMergeOptions){
        return new SimpleMenuItemHolder(item, info, clickActions, itemMergeOptions);
    }

    MenuItemHolder withItem(Holder<DynamicItem> item);
    MenuItemHolder withClickActions(ClickActions actions);
    MenuItemHolder withItemMergeOptions(Map<String, MergeOption> merge);
    MenuItemHolder withInfo(DataExchange info);

    @NotNull
    MenuItem getDisplayItem(@NotNull Entity p, @NotNull MenuContext info);

    @NotNull Holder<DynamicItem> getItem();

    @Nullable
    ClickActions getClickActions();

    @ApiStatus.Experimental
    Map<String, MergeOption> getItemMergeOptions();

    interface InfoEvaluator{
        @NotNull
        MenuContext evaluateInfo();
    }
}
