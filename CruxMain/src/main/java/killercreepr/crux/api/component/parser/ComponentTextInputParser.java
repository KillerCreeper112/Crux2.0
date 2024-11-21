package killercreepr.crux.api.component.parser;

import org.jetbrains.annotations.NotNull;

public interface ComponentTextInputParser<T> {
    @NotNull T parse(@NotNull Object object) throws IllegalArgumentException;
}
