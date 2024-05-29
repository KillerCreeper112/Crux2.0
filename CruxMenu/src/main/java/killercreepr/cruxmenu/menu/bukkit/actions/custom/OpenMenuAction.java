package killercreepr.cruxmenu.menu.bukkit.actions.custom;

import killercreepr.crux.Crux;
import killercreepr.cruxmenu.menu.bukkit.actions.ActionContext;
import killercreepr.cruxmenu.menu.bukkit.actions.SimpleMenuAction;
import killercreepr.cruxmenu.menu.bukkit.holder.MenuHolder;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class OpenMenuAction extends SimpleMenuAction {
    public OpenMenuAction(@NotNull NamespacedKey key) {
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
