package killercreepr.cruxmenus.menu.bukkit.actions.impl;

import killercreepr.crux.util.CruxMath;
import killercreepr.cruxmenus.menu.bukkit.CfgMenu;
import killercreepr.cruxmenus.menu.bukkit.actions.ActionContext;
import killercreepr.cruxmenus.menu.bukkit.actions.SimpleMenuAction;
import killercreepr.cruxmenus.menu.bukkit.module.standard.PagedMenuModule;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PagedOpenAction extends SimpleMenuAction {
    public PagedOpenAction(@NotNull Key key) {
        super(key);
    }

    @Override
    public boolean execute(@NotNull Player p, @NotNull ActionContext ctx, @NotNull String[] args) {
        String id = args[0];
        CfgMenu menu = ctx.getMenu();
        if(!(menu.getModules().getByID(id) instanceof PagedMenuModule<?> paged))
            throw new UnsupportedOperationException("CfgMenu does not have a PagedMenuModule!");

        int amount = (int) CruxMath.evaluate(args[1]);
        paged.setPage(amount);
        paged.reload(menu);
        return true;
    }
}
