package killercreepr.cruxitems.item.interaction;

import killercreepr.cruxitems.item.interaction.impl.ItemUseResultImpl;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public interface ItemUseResult {
    static ItemUseResult cancelled(){
        return new ItemUseResultImpl(true, null, null);
    }
    static ItemUseResult empty(){
        return new ItemUseResultImpl(null, null, null);
    }

    @Nullable Boolean getCancelled();
    @Nullable
    Event.Result getUseItemInHand();
    @Nullable Event.Result getUseInteractedBlock();

}
