package killercreepr.cruxitems.api.item.component;

import killercreepr.cruxitems.api.item.interaction.ItemUseContext;
import killercreepr.cruxitems.api.item.interaction.ItemUseResult;
import org.jetbrains.annotations.NotNull;

public interface InteractableComponent {
    @NotNull
    ItemUseResult onUse(@NotNull ItemUseContext ctx);
    boolean isUsable(@NotNull ItemUseContext ctx);
}
