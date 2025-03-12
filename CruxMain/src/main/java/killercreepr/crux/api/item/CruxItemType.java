package killercreepr.crux.api.item;

import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.component.TypedDataComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Consumer;

public interface CruxItemType {
    <T> @Nullable T getDefaultData(@NotNull DataComponentType<T> type);
    boolean hasDefaultData(@NotNull DataComponentType<?> type);
    @NotNull Collection<DataComponentType<?>> getDefaultData();
    void forEachDefaultData(@NotNull Consumer<TypedDataComponent<?>> consumer);
}
