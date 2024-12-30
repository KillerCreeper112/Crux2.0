package killercreepr.cruxmenus.core.menu.action.impl;

import killercreepr.crux.core.util.CruxString;
import killercreepr.cruxmenus.api.menu.contex.ActionContext;
import killercreepr.cruxmenus.core.menu.action.SimpleMenuAction;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.HumanEntity;
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
    public boolean execute(@NotNull ActionContext ctx, @NotNull String[] args) {
        if(ctx.getPlayer() instanceof Player p){
            p.performCommand(CruxString.join(args));
            return true;
        }
        return false;
    }
}
