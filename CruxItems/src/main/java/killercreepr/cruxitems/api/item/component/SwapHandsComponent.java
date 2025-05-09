package killercreepr.cruxitems.api.item.component;

import killercreepr.cruxitems.api.item.interaction.SwapHandsContext;
import killercreepr.cruxitems.api.item.interaction.SwapHandsResult;
import org.jetbrains.annotations.NotNull;

public interface SwapHandsComponent {
    @NotNull
    SwapHandsResult onSwapHands(@NotNull SwapHandsContext ctx);
    boolean isHandSwappable(@NotNull SwapHandsContext ctx);
}
