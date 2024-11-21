package killercreepr.cruxitems.api.item.inventory;

import killercreepr.cruxitems.api.item.result.GenericItemEventResult;
import killercreepr.cruxitems.core.item.inventory.ItemClickResultImpl;
import org.jetbrains.annotations.Nullable;

public interface ItemClickResult extends GenericItemEventResult {
    static ItemClickResult cancelled(){
        return new ItemClickResultImpl(true);
    }
    static ItemClickResult empty(){
        return new ItemClickResultImpl(null);
    }

    @Nullable Boolean getCancelled();

}
