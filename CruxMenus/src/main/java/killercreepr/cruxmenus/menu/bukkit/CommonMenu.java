package killercreepr.cruxmenus.menu.bukkit;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface CommonMenu {
    default void onOpen(@NotNull Player p){}
    default void onClose(@NotNull Player p){}
    default void onUpdate(){}

    default void load(){}
}
