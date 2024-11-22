package killercreepr.crux.api.component.parser;

import killercreepr.crux.core.component.parser.type.ComponentParserListType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ComponentTextInputParser<T> {
    static <T> ComponentTextInputParser<List<T>> list(@NotNull ComponentTextInputParser<T> value){
        return new ComponentParserListType<>(value);
    }

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
