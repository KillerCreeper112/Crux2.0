package killercreepr.cruxmenus.core.menu.types;

import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.ViewedMenu;
import killercreepr.cruxmenus.core.menu.BukkitViewMenu;
import killercreepr.cruxmenus.core.registries.Menus;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

public class SimpleViewMenu extends BukkitViewMenu {
    protected final MenuType type;
    public SimpleViewMenu(MenuType type) {
        this.type = type;
    }

    @Override
    public ViewedMenu reconstruct(@NotNull Entity p, @NotNull Component name) {
        return super.reconstruct(type.create((HumanEntity) p, name));
    }

    @Override
    public void onClose(@NotNull HumanEntity p) {
        Menu menu = Menus.OPENED.get(p.getUniqueId());
        super.onClose(p);
        if(menu != null){
            clearInventory(menu);
        }
    }

    public void clearInventory(Menu menu){
        menu.getInventory().clear();
    }
}
