package killercreepr.cruxmenus.core.menu.action.impl;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.core.Crux;
import killercreepr.cruxmenus.api.menu.contex.ActionContext;
import killercreepr.cruxmenus.api.menu.holder.MenuHolder;
import killercreepr.cruxmenus.core.menu.action.SimpleMenuAction;
import killercreepr.cruxmenus.core.menu.data.MenuInfoDataParser;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

public class OpenMenuAction extends SimpleMenuAction {
    public OpenMenuAction(@NotNull Key key) {
        super(key);
    }

    @Override
    public boolean execute(@NotNull ActionContext ctx, @NotNull String[] args) {
        HumanEntity p = ctx.getPlayer();
        MenuHolder menuHolder = ctx.getMenu().getHolder().getRegistry().menuHolders().get(Crux.key(args[0]));
        if(menuHolder == null) return false;
        DataExchange info;
        if(args.length > 1){
            var map = MenuInfoDataParser.parse(args[1]);
            info = ctx.getAllMergedInfo().appendObjects(map);
        }else info = ctx.getAllMergedInfo();
        menuHolder.open(p, info);
        return true;
    }
}
