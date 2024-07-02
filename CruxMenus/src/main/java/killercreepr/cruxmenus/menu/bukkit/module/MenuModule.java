package killercreepr.cruxmenus.menu.bukkit.module;

import killercreepr.cruxmenus.menu.bukkit.Menu;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MenuModule extends Keyed {
    @Nullable ActiveMenuModule build(@NotNull Menu menu);
}
