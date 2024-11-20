package killercreepr.crux.api.util;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.WeightedObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface CruxWeightedSupplierBuilder<T extends WeightedObject> {
    default CruxWeightedSupplierBuilder<T> applyContext(@NotNull LootContext context){
        luck(context);
        return filter(context);
    }
    CruxWeightedSupplierBuilder<T> luck(@NotNull LootContext context);
    CruxWeightedSupplierBuilder<T> filter(@NotNull LootContext context);
    CruxWeightedSupplierBuilder<T> pool(@NotNull Collection<T> pool);
    CruxWeightedSupplierBuilder<T> rolls(int rolls);
    CruxWeightedSupplierBuilder<T> luck(float luck);
    CruxWeightedSupplierBuilder<T> onAccepted(@Nullable Consumer<T> onAccepted);
    CruxWeightedSupplierBuilder<T> filter(@Nullable Predicate<T> filter);

    @NotNull CruxWeightedSupplier<T> build();
}
