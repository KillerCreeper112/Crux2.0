package killercreepr.crux.api.item;

import killercreepr.crux.api.component.DataComponentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface CruxItemType {
    <T> @Nullable T getDefaultData(@NotNull DataComponentType<T> type);
    boolean hasDefaultData(@NotNull DataComponentType<?> type);
    @NotNull Collection<DataComponentType<?>> getDefaultData();
}
