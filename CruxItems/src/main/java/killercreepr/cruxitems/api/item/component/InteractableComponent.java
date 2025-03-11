package killercreepr.cruxitems.api.item.component;

import killercreepr.cruxitems.api.item.interaction.ItemUseContext;
import killercreepr.cruxitems.api.item.interaction.ItemUseResult;
import org.jetbrains.annotations.NotNull;

public interface InteractableComponent {
    @NotNull
    ItemUseResult onInteract(@NotNull ItemUseContext ctx);
    boolean isInteractable(@NotNull ItemUseContext ctx);
}
