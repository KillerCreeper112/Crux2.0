package killercreepr.crux.api.component.parser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ComponentTextInputParser<T> {
    @NotNull T decodeObject(@NotNull Object object) throws IllegalArgumentException;
    default @Nullable T attemptDecodeObject(@Nullable Object object) throws IllegalArgumentException{
        if(object == null) return null;
        return decodeObject(object);
    }

    default @NotNull Object encodeObjectUnchecked(Object object){
        return encodeObject((T) object);
    }

    default @NotNull Object encodeObject(@NotNull T object){
        return object;
    }
}
