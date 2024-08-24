package killercreepr.crux.util;

import killercreepr.crux.loot.WeightedObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface CruxWeightedSupplier<T extends WeightedObject> {
    static <T extends WeightedObject> CruxWeightedSupplierBuilder<T> builder(@NotNull Collection<T> pool){
        return new SimpleWeightedSupplier.Builder<>(pool);
    }
    @NotNull List<T> rollList();

    @NotNull List<T> rollList(@Nullable Predicate<T> filter);

    @NotNull Map<T, Integer> roll();

    @NotNull Map<T, Integer> roll(@Nullable Predicate<T> filter);

    @NotNull Map<T, Integer> roll(@NotNull Consumer<T> onAccepted, @Nullable Predicate<T> filter);
}
