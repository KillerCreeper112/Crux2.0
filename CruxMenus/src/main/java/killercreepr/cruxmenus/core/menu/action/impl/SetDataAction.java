package killercreepr.cruxmenus.core.menu.action.impl;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.core.component.parser.TextDataComponentDecoder;
import killercreepr.cruxmenus.api.menu.CfgMenu;
import killercreepr.cruxmenus.api.menu.contex.ActionContext;
import killercreepr.cruxmenus.core.menu.action.SimpleMenuAction;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class SetDataAction extends SimpleMenuAction {
    public SetDataAction(@NotNull Key key) {
        super(key);
    }

    @Override
    public boolean execute(@NotNull ActionContext ctx, @NotNull String[] args) {
        var map = TextDataComponentDecoder.parseComponentMap(args[0]);
        CfgMenu menu = ctx.getMenu();
        DataExchange info = menu.info().appendObjects(map);
        menu.info(info);
        return true;
    }
}
