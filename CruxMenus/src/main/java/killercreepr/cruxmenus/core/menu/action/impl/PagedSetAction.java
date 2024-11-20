package killercreepr.cruxmenus.core.menu.action.impl;

import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxmenus.api.menu.CfgMenu;
import killercreepr.cruxmenus.api.menu.contex.ActionContext;
import killercreepr.cruxmenus.core.menu.action.SimpleMenuAction;
import killercreepr.cruxmenus.core.menu.module.standard.ActivePagedMenuModule;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PagedSetAction extends SimpleMenuAction {
    public PagedSetAction(@NotNull Key key) {
        super(key);
    }

    @Override
    public boolean execute(@NotNull Player p, @NotNull ActionContext ctx, @NotNull String[] args) {
        String id = args[0];
        CfgMenu menu = ctx.getMenu();
        if(!(menu.getModules().getByID(id) instanceof ActivePagedMenuModule<?> paged))
            throw new UnsupportedOperationException("CfgMenu does not have a PagedMenuModule! " + id);

        int amount = (int) CruxMath.evaluate(args[1]);
        paged.setPage(amount);
        return true;
    }
}
