package killercreepr.cruxadvancements.advancement.icon;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AdvancementItemIcon implements CruxAdvancementIcon{
    protected final @NotNull ItemStack item;
    public AdvancementItemIcon(@NotNull ItemStack item) {
        this.item = item;
    }

    @Override
    public @NotNull ItemStack getItem() {
        return item;
    }

    @Override
    public @Nullable CriterionDisplay getCriterionDisplay() {
        return null;
    }
}
