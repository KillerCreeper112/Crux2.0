package killercreepr.cruxadvancements.advancement.icon;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AdvancementItemIcon implements CruxAdvancementIcon{
    protected final @NotNull ItemStack item;
    public AdvancementItemIcon(@NotNull ItemStack item) {
        this.item = item;
    }

    @Override
    public @NotNull ItemStack getItem() {
        return item;
    }
}
