package killercreepr.cruxmenus.api.menu.contex;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.data.holder.DataInfoHolder;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.cruxmenus.api.menu.CfgMenu;
import killercreepr.cruxmenus.core.menu.context.SimpleMenuContext;
import org.jetbrains.annotations.NotNull;

public interface MenuContext extends DataInfoHolder {
    static MenuContext context(@NotNull CfgMenu menu, @NotNull DataExchange info, @NotNull MergedTagContainer resolvers){
        return new SimpleMenuContext(menu, info, resolvers);
    }
    @NotNull CfgMenu getMenu();

    @NotNull MergedTagContainer getResolvers();

    @NotNull DataExchange getAllMergedInfo();

    @NotNull MergedTagContainer getAllMergedResolvers();
}
