package killercreepr.cruxmenus.core.menu;

import killercreepr.cruxmenus.api.menu.ViewedMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

public class AnvilMenu extends BukkitViewMenu{
    @Override
    public ViewedMenu reconstruct(@NotNull Player p, @NotNull Component name) {
        return super.reconstruct(MenuType.ANVIL.create(p, name));
    }
}
