package killercreepr.crux.api.component;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DataComponentEditor {
    /**
     * @return The previous value, if any.
     */
    @Nullable
    <T> T set(DataComponentType<? super T> type, @Nullable T value);

    @Nullable
    default <T> T unset(DataComponentType<? super T> type){
        return set(type, null);
    }

    /**
     * @return The previous value, if any.
     */
    @Nullable
    default <T> T set(@NotNull TypedDataComponent<T> typed){
        return set(typed.getType(), typed.getValue());
    }

    void clearComponents();
}
