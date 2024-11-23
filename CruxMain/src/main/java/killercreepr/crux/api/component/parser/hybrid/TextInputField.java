package killercreepr.crux.api.component.parser.hybrid;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface TextInputField<B, T> {
    /*static <B, T> TextInputField<B, T> field(
        @NotNull PersistInputParser<T> parser,
        @NotNull Function<B, T> field
        ){
        return new TextInputField<>() {

            @Override
            public @NotNull PersistInputParser<T> inputParser() {
                return parser;
            }

            @Override
            public T parseField(B base) {
                return field.apply(base);
            }
        };
    }*/

    static <B, T> TextInputField<B, T> field(
        @NotNull PersistTextInputParser<T> parser,
        @NotNull Function<B, T> field
    ){
        return new TextInputField<>() {

            @Override
            public @NotNull PersistTextInputParser<T> inputParser() {
                return parser;
            }

            @Override
            public T parseField(B base) {
                return field.apply(base);
            }
        };
    }

    @NotNull
    PersistTextInputParser<T> inputParser();
    T parseField(B base);
}
