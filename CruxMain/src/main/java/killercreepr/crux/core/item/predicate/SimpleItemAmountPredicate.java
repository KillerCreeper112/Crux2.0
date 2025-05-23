package killercreepr.crux.core.item.predicate;

import killercreepr.crux.api.item.predicate.ItemAmountPredicate;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SimpleItemAmountPredicate implements ItemAmountPredicate {
    protected final int amount;
    public SimpleItemAmountPredicate(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean test(@NotNull ItemStack item) {
        return item.getAmount() >= amount;
    }

    @Override
    public int getAmount() {
        return amount;
    }
}
