package killercreepr.crux.api.util;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.core.util.SimpleWeightedSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface CruxWeightedSupplierBuilder<T> {
    default CruxWeightedSupplierBuilder<T> applyContext(@NotNull LootContext context){
        luck(context);
        return filter(context);
    }
    CruxWeightedSupplierBuilder<T> luck(@NotNull LootContext context);
    CruxWeightedSupplierBuilder<T> filter(@NotNull LootContext context);
    CruxWeightedSupplierBuilder<T> pool(@NotNull Collection<T> pool);
    CruxWeightedSupplierBuilder<T> rolls(int rolls);
    CruxWeightedSupplierBuilder<T> random(Random random);
    CruxWeightedSupplierBuilder<T> luck(float luck);
    CruxWeightedSupplierBuilder<T> onAccepted(@Nullable Consumer<T> onAccepted);
    CruxWeightedSupplierBuilder<T> filter(@Nullable Predicate<T> filter);
    CruxWeightedSupplierBuilder<T> weightFunction(@Nullable Function<T, Integer> filter);
    CruxWeightedSupplierBuilder<T> qualityFunction(@Nullable Function<T, Float> filter);
    SimpleWeightedSupplier.Builder<T> allowDuplicates(boolean allowDuplicates);

    @NotNull CruxWeightedSupplier<T> build();
}
