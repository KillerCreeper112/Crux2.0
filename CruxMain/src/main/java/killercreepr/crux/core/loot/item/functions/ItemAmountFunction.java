package killercreepr.crux.core.loot.item.functions;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.loot.item.ItemLootFunction;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.loot.functions.SimpleLootFunction;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Random;

public class ItemAmountFunction extends SimpleLootFunction<ItemStack> implements ItemLootFunction {
    private final @NotNull NumberProvider amount;
    private final boolean add;
    public ItemAmountFunction(@Nullable Collection<LootCondition> conditions, @NotNull NumberProvider amount, boolean add) {
        super(conditions);
        this.amount = amount;
        this.add = add;
    }


    @Override
    public ItemStack accept(@Nullable ItemStack i, @NotNull LootContext context) {
        if(i==null) return i;
        Random source = context.getRandom();
        int amount = this.amount.sample(source).intValue();
        i.setAmount(add ? (i.getAmount() + amount) : amount);
        return i;
    }
}
