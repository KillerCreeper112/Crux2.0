package killercreepr.cruxmenus.api.menu.action;

import killercreepr.cruxmenus.api.menu.contex.ActionContext;
import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface MenuAction extends Keyed {
    boolean has(@NotNull String x);
    boolean execute(@NotNull Player p, @NotNull ActionContext ctx, @NotNull String[] args);
}
