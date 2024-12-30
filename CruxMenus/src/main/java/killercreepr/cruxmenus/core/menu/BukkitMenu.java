package killercreepr.cruxmenus.core.menu;

import killercreepr.cruxmenus.api.menu.Menu;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;

public class BukkitMenu extends SimpleMenu {
    protected @NotNull Inventory inventory;

    public BukkitMenu() {
    }

    public BukkitMenu(@NotNull UUID uuid) {
        super(uuid);
    }

    public BukkitMenu(@NotNull UUID uuid, Inventory inventory) {
        super(uuid);
        this.inventory = inventory;
    }

    public Menu reconstruct(int size, @NotNull Component name, boolean keepOldContents, boolean reopenSilently){
        Inventory old = inventory;
        reconstruct(size, name);
        if(old != null){
            if(keepOldContents){
                inventory.setContents(old.getContents());
            }
            if(reopenSilently){
                for(HumanEntity e : new ArrayList<>(old.getViewers())){
                    openSilently(e);
                }
            }
        }
        return this;
    }

    @Override
    public Menu reconstruct(int size, @NotNull Component name) {
        return reconstruct(Bukkit.createInventory(this, size, name));
    }

    @Override
    public Menu reconstruct(@NotNull Inventory inv) {
        inventory = inv;
        return this;
    }

    @Override
    protected void openInventory(HumanEntity p) {
        p.openInventory(inventory);
    }

    @Override
    public void setItem(int index, @Nullable ItemStack item, boolean silent) {
        inventory.setItem(index, item);
        if(!silent) onUpdate();
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
