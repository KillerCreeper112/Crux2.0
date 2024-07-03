package killercreepr.cruxmenus.menu.bukkit.actions.impl;

import killercreepr.crux.util.CruxString;
import killercreepr.cruxmenus.menu.bukkit.actions.ActionContext;
import killercreepr.cruxmenus.menu.bukkit.actions.SimpleMenuAction;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandAction extends SimpleMenuAction {
    public CommandAction(@NotNull Key key) {
        super(key);
    }

    @Override
    public boolean has(@NotNull String x) {
        return x.equalsIgnoreCase("pcmd") || super.has(x);
    }

    @Override
    public boolean execute(@NotNull Player p, @NotNull ActionContext actionInfo, @NotNull String[] args) {
        p.performCommand(CruxString.join(args));
        return true;
    }
}
