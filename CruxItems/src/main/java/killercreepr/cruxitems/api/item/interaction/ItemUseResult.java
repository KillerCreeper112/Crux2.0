package killercreepr.cruxitems.api.item.interaction;

import killercreepr.cruxitems.api.item.result.GenericItemEventResult;
import killercreepr.cruxitems.core.item.interaction.impl.ItemUseResultImpl;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public interface ItemUseResult extends GenericItemEventResult {
    static ItemUseResult cancelled(){
        return new ItemUseResultImpl(true, null, null, true);
    }
    static ItemUseResult empty(){
        return new ItemUseResultImpl(null, null, null, true);
    }

    @Nullable
    Event.Result getUseItemInHand();
    @Nullable Event.Result getUseInteractedBlock();
    boolean successful();

}
