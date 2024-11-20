package killercreepr.crux.core.item.predicate;

import killercreepr.crux.api.item.predicate.ItemPredicate;
import killercreepr.crux.api.data.tag.Tag;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public class ItemTagPredicate implements ItemPredicate {
    protected final @NonNull Tag<ItemStack> tag;
    public ItemTagPredicate(@NonNull Tag<ItemStack> tag) {
        this.tag = tag;
    }

    @Override
    public boolean test(@NotNull ItemStack item) {
        return tag.isTagged(item);
    }
}
