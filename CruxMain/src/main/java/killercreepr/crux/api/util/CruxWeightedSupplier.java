package killercreepr.crux.api.util;

import killercreepr.crux.core.util.CruxCollection;
import killercreepr.crux.core.util.SimpleWeightedSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface CruxWeightedSupplier<T> {
    static <T> CruxWeightedSupplierBuilder<T> builder(@NotNull Collection<T> pool){
        return new SimpleWeightedSupplier.Builder<>(pool);
    }
    default @Nullable T rollFirst(@Nullable Predicate<T> filter){
        return CruxCollection.getFirst(rollList(filter));
    }
    default @Nullable T rollFirst(){
        return rollFirst(null);
    }

    @NotNull List<T> rollList();

    @NotNull List<T> rollList(@Nullable Predicate<T> filter);

    @NotNull Map<T, Integer> roll();

    @NotNull Map<T, Integer> roll(@Nullable Predicate<T> filter);

    @NotNull Map<T, Integer> roll(@NotNull Consumer<T> onAccepted, @Nullable Predicate<T> filter);
}
