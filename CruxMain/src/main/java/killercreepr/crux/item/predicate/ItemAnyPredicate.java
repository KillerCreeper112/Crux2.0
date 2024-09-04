package killercreepr.crux.item.predicate;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class ItemAnyPredicate implements ItemPredicate{
    protected final @NotNull Collection<ItemPredicate> children;
    public ItemAnyPredicate(@NotNull Collection<ItemPredicate> children) {
        this.children = children;
    }

    @Override
    public boolean test(@NotNull ItemStack item) {
        for(ItemPredicate predicate : children){
            if(predicate.test(item)) return true;
        }
        return false;
    }
}
