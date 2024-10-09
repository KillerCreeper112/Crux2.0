package killercreepr.cruxitems.item.interaction;

import killercreepr.cruxitems.item.interaction.impl.ItemUseResultImpl;
import killercreepr.cruxitems.item.result.GenericItemEventResult;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public interface ItemUseResult extends GenericItemEventResult {
    static ItemUseResult cancelled(){
        return new ItemUseResultImpl(true, null, null);
    }
    static ItemUseResult empty(){
        return new ItemUseResultImpl(null, null, null);
    }

    @Nullable
    Event.Result getUseItemInHand();
    @Nullable Event.Result getUseInteractedBlock();

}
