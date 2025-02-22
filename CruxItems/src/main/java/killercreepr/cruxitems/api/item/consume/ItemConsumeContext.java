package killercreepr.cruxitems.api.item.consume;

import killercreepr.crux.api.item.CruxItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ItemConsumeContext {
    @NotNull
    Player getPlayer();
    @NotNull
    CruxItem getItem();
    @Nullable ItemStack getReplacement();
}
