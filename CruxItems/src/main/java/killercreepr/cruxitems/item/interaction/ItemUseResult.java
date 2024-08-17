package killercreepr.cruxitems.item.interaction;

import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ItemUseResult {
    boolean isCancelled();
    @Nullable
    Event.Result getUseItemInHand();
    @Nullable Event.Result getUseInteractedBlock();

}
