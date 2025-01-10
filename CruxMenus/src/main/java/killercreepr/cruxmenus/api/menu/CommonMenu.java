package killercreepr.cruxmenus.api.menu;

import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

public interface CommonMenu {
    default void onOpen(@NotNull HumanEntity p){}
    default void onClose(@NotNull HumanEntity p){}
    default void onUpdate(){}

    default void load(){}
}
