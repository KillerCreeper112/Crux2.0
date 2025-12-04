package killercreepr.crux.core.loot.conditions.item;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.core.loot.conditions.BaseCondition;
import killercreepr.crux.core.util.CruxInv;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InventoryHasItemCondition extends BaseCondition {
    protected final @Nullable LootCondition itemCondition;
    protected final int amount;

    public InventoryHasItemCondition(@NotNull String target, @Nullable LootCondition itemCondition, int amount) {
        super(target);
        this.itemCondition = itemCondition;
        this.amount = amount;
    }

    public boolean checkItemCondition(Inventory inv, LootContext ctx){
        return CruxInv.hasItems(inv, item ->{
            return itemCondition.test(ctx.withInfo(ctx.info().append("this", Holder.directObject(item))));
        }, amount);
    }

    @Override
    public boolean test(@NotNull LootContext ctx) {
        Inventory inv = ctx.info().getWithDefault(target, Inventory.class, i ->{
            var holder = i.get(target, InventoryHolder.class);
            if(holder != null) return holder.getInventory();
            return null;
        });
        if(inv==null) return false;
        if(itemCondition != null){
            if(!checkItemCondition(inv, ctx)) return false;
        }
        return true;
    }
}
