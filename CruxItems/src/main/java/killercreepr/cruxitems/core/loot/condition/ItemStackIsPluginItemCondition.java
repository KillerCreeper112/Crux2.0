package killercreepr.cruxitems.core.loot.condition;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.core.loot.conditions.BaseCondition;
import killercreepr.cruxitems.api.item.plugin.PluginItem;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemStackIsPluginItemCondition extends BaseCondition {
    public ItemStackIsPluginItemCondition(@NotNull String target) {
        super(target);
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        ItemStack item = ctx.info().get(target, ItemStack.class);
        if(item == null && ctx.info().get(target) instanceof Item it) item = it.getItemStack();
        if(item==null) return false;
        return PluginItem.isPluginItem(item);
    }
}
