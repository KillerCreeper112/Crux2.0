package killercreepr.cruxmenus.core.menu;

import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.ViewedMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BukkitViewMenu extends SimpleMenu implements ViewedMenu {
    protected InventoryView view;

    public BukkitViewMenu() {
    }

    public BukkitViewMenu(@NotNull UUID uuid) {
        super(uuid);
    }

    public BukkitViewMenu(InventoryView view) {
        this.view = view;
    }

    public BukkitViewMenu(@NotNull UUID uuid, InventoryView view) {
        super(uuid);
        this.view = view;
    }

    @Override
    protected void openInventory(HumanEntity p) {
        p.openInventory(view);
    }

    @Override
    public Menu reconstruct(int size, @NotNull Component name) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public Menu reconstruct(@NotNull Inventory inv) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public void setItem(int index, @Nullable ItemStack item, boolean silent) {
        view.setItem(index, item);
        if(!silent) onUpdate();
    }

    @Override
    public @NotNull InventoryView getView() {
        return view;
    }

    @Override
    public ViewedMenu reconstruct(@NotNull Entity p, @NotNull Component name) {
        return reconstruct(MenuType.GENERIC_9X6.create((HumanEntity) p, name));
    }

    @Override
    public ViewedMenu reconstruct(@NotNull InventoryView inv) {
        if(view != null) getInventory().clear();
        this.view = inv;
        getSlots().values().forEach(slot ->{
            ItemStack item = slot.getSlottedItemReplacement();
            if(item == null) return;
            slot.setItem(item, true);
        });
        return this;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return view.getTopInventory();
    }
}
