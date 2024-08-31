package killercreepr.crux.item.predicate;

import killercreepr.crux.Crux;
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
