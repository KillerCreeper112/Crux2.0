package killercreepr.cruxmenus.menu.bukkit.module;

import killercreepr.crux.data.Identifiable;
import killercreepr.crux.data.StringIdentifiable;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.cruxmenus.menu.bukkit.Menu;
import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MenuModule extends Keyed, StringIdentifiable {
    default void onOpen(@NotNull Player p, @NotNull Menu menu){}
    default void onClose(@NotNull Player p, @NotNull Menu menu){}
    default void onUpdate(@NotNull Menu menu){}

    default void load(@NotNull Menu menu){}
    default @Nullable MergedTagContainer buildTags(@NotNull Menu menu){ return null; }
}
