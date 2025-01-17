package killercreepr.crux.api.component.parser.hybrid;

import killercreepr.crux.core.component.parser.hybrid.SimpleTextInputField;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface TextInputField<B, T> {
    static <B, T> TextInputField<B, T> field(
        @NotNull PersistTextParser<T> parser,
        @NotNull Function<B, T> field
    ){
        return new SimpleTextInputField<>(parser, field);
    }

    static <B, T> TextInputField<B, T> field(
        @NotNull Class<B> baseType,
        @NotNull PersistTextParser<T> parser,
        @NotNull Function<B, T> field
    ){
        return new SimpleTextInputField<>(parser, field);
    }

    static <B, T> TextInputField<B, T> fieldHolder(Class<B> type,
                                                   Function<B, PersistTextParser> baseObject,
                                                   Function<Object, PersistTextParser> object){
        return new SimpleTextInputField.Holder<>((Function) baseObject, (Function) object);
    }

    @NotNull
    PersistTextParser<T> inputParser();
    T parseField(B base);

    default T parseFieldObject(Object base){
        return parseField((B) base);
    }

    interface Holder<B, T> extends TextInputField<B, T>{
        default T parseField(B base){
            throw new UnsupportedOperationException("Holder");
        }
        default @NotNull
        PersistTextParser<T> inputParser(){
            throw new UnsupportedOperationException("Holder");
        }

        default PersistTextParser<T> getInputParser(Object object){
            try{
                B value = (B) object;
                return inputParser(value);
            }catch (ClassCastException ignored){
                return inputParserFromObject(object);
            }
        }

        PersistTextParser<T> inputParser(B object);
        PersistTextParser<T> inputParserFromObject(Object object);
    }
}
