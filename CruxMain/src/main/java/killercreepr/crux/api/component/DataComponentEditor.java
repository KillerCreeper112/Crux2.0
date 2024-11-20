package killercreepr.crux.api.component;

import org.jetbrains.annotations.Nullable;


public interface DataComponentEditor {
    @Nullable
    <T> T set(DataComponentType<? super T> type, @Nullable T value);
}
