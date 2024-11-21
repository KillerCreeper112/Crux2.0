package killercreepr.cruxitems.api.item.interaction;

import org.jetbrains.annotations.NotNull;

public interface InteractableItem {
    @NotNull ItemUseResult onUse(@NotNull ItemUseContext ctx);
}
