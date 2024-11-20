package killercreepr.cruxmenus.core.menu.action.impl;

import killercreepr.crux.core.Crux;
import killercreepr.cruxmenus.api.menu.contex.ActionContext;
import killercreepr.cruxmenus.api.menu.holder.MenuHolder;
import killercreepr.cruxmenus.core.menu.action.SimpleMenuAction;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class OpenMenuAction extends SimpleMenuAction {
    public OpenMenuAction(@NotNull Key key) {
        super(key);
    }

    @Override
    public boolean execute(@NotNull Player p, @NotNull ActionContext context, @NotNull String[] args) {
        MenuHolder menuHolder = context.getMenu().getHolder().getRegistry().menuHolders().get(Crux.key(args[0]));
        if(menuHolder == null) return false;
        menuHolder.open(p, context.getAllMergedInfo());
        return true;
    }
}
