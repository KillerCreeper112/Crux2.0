package killercreepr.cruxitems.item.interaction;

import org.jetbrains.annotations.NotNull;

public interface InteractableItem {
    @NotNull ItemUseResult onUse(@NotNull ItemUseContext ctx);
}
