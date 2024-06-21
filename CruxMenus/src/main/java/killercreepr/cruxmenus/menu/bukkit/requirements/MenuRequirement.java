package killercreepr.cruxmenus.menu.bukkit.requirements;

import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface MenuRequirement extends Keyed {
    boolean has(@NotNull String x);
    boolean test(@NotNull Player p, @NotNull RequirementContext info, @NotNull String[] args);
}
