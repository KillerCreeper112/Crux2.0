package killercreepr.cruxmenu.menu.bukkit.actions.impl;

import killercreepr.cruxmenu.menu.bukkit.actions.ActionContext;
import killercreepr.cruxmenu.menu.bukkit.actions.SimpleMenuAction;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandAction extends SimpleMenuAction {
    public CommandAction(@NotNull NamespacedKey key) {
        super(key);
    }

    @Override
    public boolean execute(@NotNull Player p, @NotNull ActionContext actionInfo, @NotNull String[] args) {
        p.performCommand(args[0]);
        return true;
    }
}
