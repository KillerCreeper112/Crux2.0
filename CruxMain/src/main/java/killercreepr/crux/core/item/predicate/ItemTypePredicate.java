package killercreepr.crux.core.item.predicate;

import killercreepr.crux.api.item.ItemListHolder;
import killercreepr.crux.api.item.predicate.ItemPredicate;
import killercreepr.crux.core.Crux;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemTypePredicate implements ItemPredicate, ItemListHolder {
    protected final @NotNull Key type;
    public ItemTypePredicate(@NotNull Key type) {
        this.type = type;
    }

    @Override
    public boolean test(@NotNull ItemStack item) {
        return Crux.handlers().item().getType(item).equals(type);
    }

    @Override
    public @NotNull List<ItemStack> getItemValues() {
        var i = Crux.handlers().item().getItem(type);
        if(i == null) return List.of();
        return List.of(i.value());
    }
}
