package killercreepr.crux.core.component.parser.hybrid;

import killercreepr.crux.api.component.parser.hybrid.PersistTextInputParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class SimpleTextInputField<B, T> implements TextInputField<B, T> {
    protected final @NotNull PersistTextInputParser<T> parser;
    protected final @NotNull Function<B, T> field;

    public SimpleTextInputField(@NotNull PersistTextInputParser<T> parser, @NotNull Function<B, T> field) {
        this.parser = parser;
        this.field = field;
    }

    @Override
    public @NotNull PersistTextInputParser<T> inputParser() {
        return parser;
    }

    @Override
    public T parseField(B base) {
        return field.apply(base);
    }
}
