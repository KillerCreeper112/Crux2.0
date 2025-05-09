package killercreepr.cruxitems.api.item.interaction;

import org.jetbrains.annotations.NotNull;

public interface SwapHandsItem {
    @NotNull SwapHandsResult onSwapHands(@NotNull SwapHandsContext ctx);
}
