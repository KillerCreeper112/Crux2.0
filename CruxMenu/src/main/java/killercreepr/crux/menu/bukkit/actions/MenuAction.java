package killercreepr.crux.menu.bukkit.actions;

import org.bukkit.Keyed;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface MenuAction extends Keyed {
    boolean has(@NotNull String x);
    boolean execute(@NotNull Player p, @NotNull ActionInfo info, @NotNull String[] args);
}
