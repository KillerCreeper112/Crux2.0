package killercreepr.cruxitems.item.inventory;

import killercreepr.cruxitems.item.interaction.ItemUseResult;
import org.jetbrains.annotations.NotNull;

public interface InventoryItem {
    @NotNull
    ItemUseResult onClick(@NotNull ItemClickContext ctx);
}
