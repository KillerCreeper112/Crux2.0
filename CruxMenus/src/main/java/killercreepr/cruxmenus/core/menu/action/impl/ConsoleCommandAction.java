package killercreepr.cruxmenus.core.menu.action.impl;

import killercreepr.crux.core.util.CruxString;
import killercreepr.cruxmenus.api.menu.contex.ActionContext;
import killercreepr.cruxmenus.core.menu.action.SimpleMenuAction;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
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
    public boolean execute(@NotNull ActionContext actionInfo, @NotNull String[] args) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),  CruxString.join(args));
        return true;
    }
}
