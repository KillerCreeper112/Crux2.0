package killercreepr.crux.api.component.parser.hybrid;

import killercreepr.crux.core.component.parser.hybrid.SimpleTextInputField;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface TextInputField<B, T> {
    static <B, T> TextInputField<B, T> field(
        @NotNull PersistTextInputParser<T> parser,
        @NotNull Function<B, T> field
    ){
        return new SimpleTextInputField<>(parser, field);
    }

    static <B, T> TextInputField<B, T> field(
        @NotNull Class<B> baseType,
        @NotNull PersistTextInputParser<T> parser,
        @NotNull Function<B, T> field
    ){
        return new SimpleTextInputField<>(parser, field);
    }

    @NotNull
    PersistTextInputParser<T> inputParser();
    T parseField(B base);
}
