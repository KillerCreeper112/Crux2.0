package killercreepr.crux.item.predicate;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class ItemAllPredicate implements ItemPredicate{
    protected final @NotNull Collection<ItemPredicate> children;
    public ItemAllPredicate(@NotNull Collection<ItemPredicate> children) {
        this.children = children;
    }

    @Override
    public boolean test(@NotNull ItemStack item) {
        for(ItemPredicate predicate : children){
            if(!predicate.test(item)) return false;
        }
        return true;
    }
}
