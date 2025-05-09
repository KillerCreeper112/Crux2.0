package killercreepr.cruxitems.api.item.interaction;

import killercreepr.cruxitems.api.item.result.GenericItemEventResult;
import killercreepr.cruxitems.core.item.interaction.impl.ItemUseResultImpl;
import killercreepr.cruxitems.core.item.interaction.impl.SwapHandsResultImpl;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public interface SwapHandsResult extends GenericItemEventResult {
    static SwapHandsResult cancelled(){
        return new SwapHandsResultImpl(true, true);
    }
    static SwapHandsResult empty(){
        return new SwapHandsResultImpl(null, true);
    }

    boolean successful();
}
