package killercreepr.crux.core.item.predicate;

import killercreepr.crux.api.item.ItemListHolder;
import killercreepr.crux.api.item.predicate.ItemPredicate;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemInvertPredicate implements ItemPredicate, ItemListHolder {
    protected final @NotNull ItemPredicate child;
    public ItemInvertPredicate(@NotNull ItemPredicate child) {
        this.child = child;
    }

    @Override
    public boolean test(@NotNull ItemStack item) {
        return !child.test(item);
    }

    @Override
    public @NotNull List<ItemStack> getItemValues() {
        if(child instanceof ItemListHolder list) return list.getItemValues();
        return List.of();
    }
}
