package killercreepr.crux.core.component.parser.hybrid;

import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class HolderTextInputField<B, T> implements TextInputField<B, T> {
    protected final @NotNull PersistTextParser<T> parser;

    public HolderTextInputField(@NotNull PersistTextParser<T> parser) {
        this.parser = parser;
    }

    @Override
    public @NotNull PersistTextParser<T> inputParser() {
        return parser;
    }

    @Override
    public T parseField(B base) {
        throw new UnsupportedOperationException("");
    }
}
