package killercreepr.cruxmenus.core.menu.action.impl;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.cruxmenus.api.menu.CfgMenu;
import killercreepr.cruxmenus.api.menu.contex.ActionContext;
import killercreepr.cruxmenus.core.menu.action.SimpleMenuAction;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class RemoveDataAction extends SimpleMenuAction {
    public RemoveDataAction(@NotNull Key key) {
        super(key);
    }

    @Override
    public boolean execute(@NotNull ActionContext ctx, @NotNull String[] args) {
        CfgMenu menu = ctx.getMenu();
        DataExchange info = menu.info().remove(args);
        menu.info(info);
        return true;
    }
}
