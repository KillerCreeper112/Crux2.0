package killercreepr.cruxadvancements.advancement.icon;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CruxAdvancementIcon {
    @NotNull
    ItemStack getItem();
    @Nullable CriterionDisplay getCriterionDisplay();
}
