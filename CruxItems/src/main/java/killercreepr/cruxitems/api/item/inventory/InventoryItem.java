package killercreepr.cruxitems.api.item.inventory;

import killercreepr.cruxitems.api.item.interaction.ItemUseResult;
import org.jetbrains.annotations.NotNull;

public interface InventoryItem {
    @NotNull
    ItemUseResult onClick(@NotNull ItemClickContext ctx);
}
