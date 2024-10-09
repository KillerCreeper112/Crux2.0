package killercreepr.cruxitems.item.inventory;

import killercreepr.cruxitems.item.inventory.impl.ItemClickResultImpl;
import killercreepr.cruxitems.item.result.GenericItemEventResult;
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
