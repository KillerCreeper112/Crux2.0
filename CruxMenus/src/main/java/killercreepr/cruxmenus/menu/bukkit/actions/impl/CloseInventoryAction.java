package killercreepr.cruxmenus.menu.bukkit.actions.impl;

import killercreepr.cruxmenus.menu.bukkit.actions.ActionContext;
import killercreepr.cruxmenus.menu.bukkit.actions.SimpleMenuAction;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CloseInventoryAction extends SimpleMenuAction {
    public CloseInventoryAction(@NotNull Key key) {
        super(key);
    }

    @Override
    public boolean execute(@NotNull Player p, @NotNull ActionContext actionInfo, @NotNull String[] args) {
        p.closeInventory();
        return true;
    }
}
