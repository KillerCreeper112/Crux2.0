package killercreepr.cruxmenus.api.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

public interface ViewedMenu extends Menu {
    @NotNull InventoryView getView();
    ViewedMenu reconstruct(@NotNull HumanEntity p, @NotNull Component name);
    ViewedMenu reconstruct(@NotNull InventoryView inv);
}
