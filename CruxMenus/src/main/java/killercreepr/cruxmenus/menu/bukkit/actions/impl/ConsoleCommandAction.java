package killercreepr.cruxmenus.menu.bukkit.actions.impl;

import killercreepr.crux.util.CruxString;
import killercreepr.cruxmenus.menu.bukkit.actions.ActionContext;
import killercreepr.cruxmenus.menu.bukkit.actions.SimpleMenuAction;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ConsoleCommandAction extends SimpleMenuAction {
    public ConsoleCommandAction(@NotNull Key key) {
        super(key);
    }

    @Override
    public boolean has(@NotNull String x) {
        return x.equalsIgnoreCase("ccmd") || super.has(x);
    }

    @Override
    public boolean execute(@NotNull Player p, @NotNull ActionContext actionInfo, @NotNull String[] args) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),  CruxString.join(args));
        return true;
    }
}
