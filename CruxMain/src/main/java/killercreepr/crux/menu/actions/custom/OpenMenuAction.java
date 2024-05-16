package killercreepr.crux.menu.actions.custom;

import killerceepr.crux.Crux;
import killerceepr.crux.menu.actions.ActionInfo;
import killerceepr.crux.menu.actions.SimpleMenuAction;
import killerceepr.crux.menu.holder.MenuHolder;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class OpenMenuAction extends SimpleMenuAction {
    public OpenMenuAction(@NotNull NamespacedKey key) {
        super(key);
    }

    @Override
    public boolean execute(@NotNull Player p, @NotNull ActionInfo actionInfo, @NotNull String[] args) {
        MenuHolder menuHolder = Crux.inst().getMenuRegistry().MENU_HOLDERS.get(Crux.key(args[0]));
        if(menuHolder == null) return false;
        menuHolder.open(p, actionInfo.getInfo());
        return true;
    }
}
