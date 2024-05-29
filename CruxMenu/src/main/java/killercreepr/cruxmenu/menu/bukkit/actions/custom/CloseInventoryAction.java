package killercreepr.cruxmenu.menu.bukkit.actions.custom;

import killercreepr.cruxmenu.menu.bukkit.actions.ActionContext;
import killercreepr.cruxmenu.menu.bukkit.actions.SimpleMenuAction;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CloseInventoryAction extends SimpleMenuAction {
    public CloseInventoryAction(@NotNull NamespacedKey key) {
        super(key);
    }

    @Override
    public boolean execute(@NotNull Player p, @NotNull ActionContext actionInfo, @NotNull String[] args) {
        p.closeInventory();
        return true;
    }
}
