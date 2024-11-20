package killercreepr.crux.api.component.serialization;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ComponentSerializer<I, V> {
    default V decodeUnchecked(@NotNull Object o){
        return decode((I) o);
    }

    default V encodeUnchecked(@NotNull Object to, @Nullable Object value){
        return encode((I) to, (V) value);
    }

    @Nullable
    V decode(@NotNull I from);
    @Nullable V encode(@NotNull I to, @Nullable V value);
}
