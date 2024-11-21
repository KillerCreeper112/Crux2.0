package killercreepr.crux.api.component.parser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ComponentTextInputParser<T> {
    @NotNull T parse(@NotNull Object object) throws IllegalArgumentException;
    default @Nullable T attemptParse(@Nullable Object object) throws IllegalArgumentException{
        if(object == null) return null;
        return parse(object);
    }
}
