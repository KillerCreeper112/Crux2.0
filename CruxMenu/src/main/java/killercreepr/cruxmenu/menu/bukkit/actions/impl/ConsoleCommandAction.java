package killercreepr.cruxmenu.menu.bukkit.actions.impl;

import killercreepr.cruxmenu.menu.bukkit.actions.ActionContext;
import killercreepr.cruxmenu.menu.bukkit.actions.SimpleMenuAction;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ConsoleCommandAction extends SimpleMenuAction {
    public ConsoleCommandAction(@NotNull NamespacedKey key) {
        super(key);
    }

    @Override
    public boolean execute(@NotNull Player p, @NotNull ActionContext actionInfo, @NotNull String[] args) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), args[0]);
        return true;
    }
}
