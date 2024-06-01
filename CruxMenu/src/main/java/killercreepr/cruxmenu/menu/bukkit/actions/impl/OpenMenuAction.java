package killercreepr.cruxmenu.menu.bukkit.actions.impl;

import killercreepr.crux.Crux;
import killercreepr.cruxmenu.menu.bukkit.actions.ActionContext;
import killercreepr.cruxmenu.menu.bukkit.actions.SimpleMenuAction;
import killercreepr.cruxmenu.menu.bukkit.holder.MenuHolder;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class OpenMenuAction extends SimpleMenuAction {
    public OpenMenuAction(@NotNull Key key) {
        super(key);
    }

    @Override
    public boolean execute(@NotNull Player p, @NotNull ActionContext context, @NotNull String[] args) {
        MenuHolder menuHolder = context.getMenu().getHolder().getRegistry().MENU_HOLDERS.get(Crux.key(args[0]));
        if(menuHolder == null) return false;
        menuHolder.open(p, context.getInfo());
        return true;
    }
}
