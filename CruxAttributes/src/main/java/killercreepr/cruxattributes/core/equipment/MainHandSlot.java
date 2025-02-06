package killercreepr.cruxattributes.core.equipment;

import killercreepr.cruxattributes.api.equipment.CruxSlot;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public class MainHandSlot implements CruxSlot {
    protected final Key key;

    public MainHandSlot(Key key) {
        this.key = key;
    }

    @Override
    public boolean wouldActivate(@NotNull Entity p, int slot) {
        return slot == getIndex(p);
    }

    @Override
    public int getIndex(@NotNull Entity e) {
        if(e instanceof InventoryHolder p && p.getInventory() instanceof PlayerInventory inv) return inv.getHeldItemSlot();
        return 0;
    }

    @Override
    public @NotNull String translateKey() {
        return "cruxslot." + key.asString();
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
