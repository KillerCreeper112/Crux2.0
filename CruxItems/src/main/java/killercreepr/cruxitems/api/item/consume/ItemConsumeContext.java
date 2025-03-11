package killercreepr.cruxitems.api.item.consume;

import killercreepr.cruxitems.api.item.context.PlayerItemedContext;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ItemConsumeContext extends PlayerItemedContext {
    @Nullable ItemStack getReplacement();
    @NotNull EquipmentSlot getSlot();
}
