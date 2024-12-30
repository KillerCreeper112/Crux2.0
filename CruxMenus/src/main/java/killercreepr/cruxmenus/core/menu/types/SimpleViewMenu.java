package killercreepr.cruxmenus.core.menu.types;

import killercreepr.cruxmenus.api.menu.ViewedMenu;
import killercreepr.cruxmenus.core.menu.BukkitViewMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.Registry;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

public class SimpleViewMenu extends BukkitViewMenu {
    protected final MenuType type;
    public SimpleViewMenu(MenuType type) {
        this.type = type;
    }

    @Override
    public ViewedMenu reconstruct(@NotNull HumanEntity p, @NotNull Component name) {
        return super.reconstruct(type.create(p, name));
    }
}
