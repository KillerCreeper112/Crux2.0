package killercreepr.crux.core.item.predicate;

import killercreepr.crux.api.item.predicate.ItemPredicate;
import killercreepr.crux.core.Crux;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemTypePredicate implements ItemPredicate {
    protected final @NotNull Key type;
    public ItemTypePredicate(@NotNull Key type) {
        this.type = type;
    }

    @Override
    public boolean test(@NotNull ItemStack item) {
        return Crux.handlers().item().getType(item).equals(type);
    }
}
