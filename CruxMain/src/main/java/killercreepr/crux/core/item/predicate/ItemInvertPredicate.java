package killercreepr.crux.core.item.predicate;

import killercreepr.crux.api.item.predicate.ItemPredicate;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemInvertPredicate implements ItemPredicate {
    protected final @NotNull ItemPredicate child;
    public ItemInvertPredicate(@NotNull ItemPredicate child) {
        this.child = child;
    }

    @Override
    public boolean test(@NotNull ItemStack item) {
        return !child.test(item);
    }
}
