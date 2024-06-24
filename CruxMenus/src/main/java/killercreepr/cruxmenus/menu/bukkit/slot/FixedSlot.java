package killercreepr.cruxmenus.menu.bukkit.slot;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FixedSlot extends Slot{
    @Override
    default boolean mayPlace(@NotNull HumanEntity p, @Nullable ItemStack item) {
        return false;
    }

    @Override
    default boolean mayTake(@NotNull HumanEntity p, @Nullable ItemStack item) {
        return false;
    }
}
