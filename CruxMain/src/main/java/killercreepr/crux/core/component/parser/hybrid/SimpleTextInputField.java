package killercreepr.crux.core.component.parser.hybrid;

import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class SimpleTextInputField<B, T> implements TextInputField<B, T> {
    protected final @NotNull PersistTextParser<T> parser;
    protected final @NotNull Function<B, T> field;

    public SimpleTextInputField(@NotNull PersistTextParser<T> parser, @NotNull Function<B, T> field) {
        this.parser = parser;
        this.field = field;
    }

    @Override
    public @NotNull PersistTextParser<T> inputParser() {
        return parser;
    }

    @Override
    public T parseField(B base) {
        return field.apply(base);
    }

    public static class Holder<B, T> implements TextInputField.Holder<B, T>{
        protected final Function<B, PersistTextParser<T>> baseObject;
        protected final Function<Object, PersistTextParser<T>> object;

        public Holder(Function<B, PersistTextParser<T>> baseObject, Function<Object, PersistTextParser<T>> object) {
            this.baseObject = baseObject;
            this.object = object;
        }

        @Override
        public PersistTextParser<T> inputParser(B object) {
            return baseObject.apply(object);
        }

        @Override
        public PersistTextParser<T> inputParserFromObject(Object object) {
            return this.object.apply(object);
        }
    }
}
