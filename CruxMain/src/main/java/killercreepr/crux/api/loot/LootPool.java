package killercreepr.crux.api.loot;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public interface LootPool<T> extends ConditionedObject, FunctionedObject<T>  {
    default @NotNull Collection<T> populateLoot(@NotNull LootContext context) {
        return populateLoot(context, null, false);
    }

    @NotNull Collection<T> populateLoot(@NotNull LootContext context, @Nullable Predicate<LootPoolObject<T>> exclude, boolean excludeEmpty);

    boolean isEmptyWith(@Nullable Predicate<LootPoolObject<T>> exclude);

    @NotNull List<LootPoolObject<T>> random(@NotNull List<LootPoolObject<T>> data, int rolls, @NotNull LootContext context, @Nullable Predicate<LootPoolObject<T>> exclude);
}
