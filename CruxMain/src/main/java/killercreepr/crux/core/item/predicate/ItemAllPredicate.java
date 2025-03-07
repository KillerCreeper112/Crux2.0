package killercreepr.crux.core.item.predicate;

import killercreepr.crux.api.item.ItemListHolder;
import killercreepr.crux.api.item.predicate.ItemPredicate;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemAllPredicate implements ItemPredicate, ItemListHolder {
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

    @Override
    public @NotNull List<ItemStack> getItemValues() {
        List<ItemStack> list = new ArrayList<>();
        for(ItemPredicate predicate : children){
            if(predicate instanceof ItemListHolder holder){
                list.addAll(holder.getItemValues());
            }
        }
        return list;
    }
}
