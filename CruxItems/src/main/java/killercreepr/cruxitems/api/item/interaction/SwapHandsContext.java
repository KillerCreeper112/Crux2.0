package killercreepr.cruxitems.api.item.interaction;

import killercreepr.cruxitems.api.item.context.PlayerItemedContext;
import org.bukkit.inventory.ItemStack;

public interface SwapHandsContext extends PlayerItemedContext {
    ItemStack getMainHandItem();
    ItemStack getOffhandItem();
}
