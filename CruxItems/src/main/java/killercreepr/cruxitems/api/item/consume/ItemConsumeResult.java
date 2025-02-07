package killercreepr.cruxitems.api.item.consume;

import killercreepr.cruxitems.api.item.result.GenericItemEventResult;
import killercreepr.cruxitems.core.item.consume.ItemConsumeResultImpl;
import killercreepr.cruxitems.core.item.interaction.impl.ItemUseResultImpl;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface ItemConsumeResult extends GenericItemEventResult {
    static ItemConsumeResult cancelled(){
        return new ItemConsumeResultImpl(true, null, true, false, null, false);
    }
    static ItemConsumeResult empty(){
        return new ItemConsumeResultImpl(null, null,  true, false, null, false);
    }

    boolean successful();
    boolean replaceResultItem();
    boolean replaceItem();
    ItemStack resultItem();
    ItemStack item();

    interface Builder{
        Builder cancel(Boolean cancel);
        Builder successful(boolean successful);
        Builder resultItem(ItemStack resultItem);
    }
}
