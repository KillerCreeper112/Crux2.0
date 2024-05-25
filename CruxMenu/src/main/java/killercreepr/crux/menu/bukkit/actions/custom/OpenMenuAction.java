package killercreepr.crux.menu.bukkit.actions.custom;

import killercreepr.crux.menu.bukkit.actions.ActionInfo;
import killercreepr.crux.menu.bukkit.actions.SimpleMenuAction;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class OpenMenuAction extends SimpleMenuAction {
    public OpenMenuAction(@NotNull NamespacedKey key) {
        super(key);
    }

    @Override
    public boolean execute(@NotNull Player p, @NotNull ActionInfo actionInfo, @NotNull String[] args) {
        //todo
        /*MenuHolder menuHolder = Crux.inst().getMenuRegistry().MENU_HOLDERS.get(Crux.key(args[0]));
        if(menuHolder == null) return false;
        menuHolder.open(p, actionInfo.getInfo());*/
        return true;
    }
}
