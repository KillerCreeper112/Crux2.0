package killercreepr.crux.api.component.serialization;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ComponentSerializer<I, V> {
    default V decodeUnchecked(@NotNull Object o){
        return decode((I) o);
    }

    default void encodeUnchecked(@NotNull Object to, @Nullable Object value){
        encode((I) to, (V) value);
    }

    @Nullable
    V decode(@NotNull I from);
    void encode(@NotNull I to, @Nullable V value);
}
