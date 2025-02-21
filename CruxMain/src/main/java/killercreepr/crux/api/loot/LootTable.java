package killercreepr.crux.api.loot;

import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public interface LootTable<T> extends Keyed {
    @NotNull List<T> populateLoot(@NotNull LootContext context);
    @NotNull List<T> populateLoot(@NotNull LootContext context, @Nullable Predicate<LootPoolObject<T>> exclude, boolean excludeEmpty);
    @NotNull List<T> populateLoot(@NotNull LootContext context, @Nullable Predicate<LootPoolObject<T>> exclude, boolean excludeEmpty,
                                  @Nullable Function<LootPool<T>,Collection<T>> poolFunction);
    //void fillInventory(@NotNull Inventory inventory, @NotNull GrimLootContext context);
    /*@NotNull
    List<LootPool<T>> random(int rolls, @NotNull LootContext context);*/
}
