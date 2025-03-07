package killercreepr.crux.core.item.predicate;

import killercreepr.crux.api.data.tag.Tag;
import killercreepr.crux.api.item.ItemListHolder;
import killercreepr.crux.api.item.predicate.ItemPredicate;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemTagPredicate implements ItemPredicate, ItemListHolder {
    protected final @NotNull Tag<ItemStack> tag;
    public ItemTagPredicate(@NotNull Tag<ItemStack> tag) {
        this.tag = tag;
    }

    @Override
    public boolean test(@NotNull ItemStack item) {
        return tag.isTagged(item);
    }

    @Override
    public @NotNull List<ItemStack> getItemValues() {
        if(tag instanceof ItemListHolder list) return list.getItemValues();
        return List.of();
    }
}
