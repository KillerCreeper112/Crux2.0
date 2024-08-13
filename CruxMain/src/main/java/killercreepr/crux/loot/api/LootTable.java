package killercreepr.crux.loot.api;

import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public interface LootTable<T> extends Keyed {
    @NotNull Collection<T> populateLoot(@NotNull LootContext context);
    @NotNull Collection<T> populateLoot(@NotNull LootContext context, @Nullable Predicate<LootPoolObject<T>> exclude, boolean excludeEmpty);
    //void fillInventory(@NotNull Inventory inventory, @NotNull GrimLootContext context);
    @NotNull
    List<LootPool<T>> random(int rolls, @NotNull LootContext context);
}
