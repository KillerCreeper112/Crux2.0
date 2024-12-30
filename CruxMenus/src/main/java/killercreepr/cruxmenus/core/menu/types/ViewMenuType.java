package killercreepr.cruxmenus.core.menu.types;

import killercreepr.crux.api.data.CruxKeyed;
import killercreepr.cruxmenus.api.menu.ViewedMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

public interface ViewMenuType extends CruxKeyed {
    @NotNull ViewedMenu create(@NotNull HumanEntity viewer, @NotNull Component title);
}
