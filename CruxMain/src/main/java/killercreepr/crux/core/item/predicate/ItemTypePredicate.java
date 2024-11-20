package killercreepr.crux.core.item.predicate;

import killercreepr.crux.core.Crux;
import killercreepr.crux.api.item.predicate.ItemPredicate;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public class ItemTypePredicate implements ItemPredicate {
    protected final @NonNull Key type;
    public ItemTypePredicate(@NonNull Key type) {
        this.type = type;
    }

    @Override
    public boolean test(@NotNull ItemStack item) {
        return Crux.handlers().item().getType(item).equals(type);
    }
}
