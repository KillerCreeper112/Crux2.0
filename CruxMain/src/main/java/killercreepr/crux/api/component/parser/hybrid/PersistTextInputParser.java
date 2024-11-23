package killercreepr.crux.api.component.parser.hybrid;

import killercreepr.crux.api.component.parser.ComponentTextInputParser;
import killercreepr.crux.api.component.parser.standard.ComponentParserListTypeHolder;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.component.parser.hybrid.SimplePersistInputParser;
import killercreepr.crux.core.component.parser.hybrid.text.ElementPersistTextInputParser;
import killercreepr.crux.core.component.parser.hybrid.text.ListPersistTextInputParser;
import killercreepr.crux.core.component.parser.hybrid.text.MapPersistTextInputParser;
import killercreepr.crux.core.component.parser.hybrid.text.PrimitivePersistTextInputParser;
import killercreepr.crux.core.persistence.CruxPersistence;
import net.kyori.adventure.key.Key;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public interface PersistTextInputParser<T> extends ComponentTextInputParser<T>{
    static <T> MapBuilder<T> mapBuilder(){
        return new MapPersistTextInputParser.Builder<T>();
    }

    static <T> MapBuilder<T> mapBuilder(Class<T> type){
        return new MapPersistTextInputParser.Builder<T>().dataTypeClass(type);
    }

    static <T> ElementBuilder<T> elementBuilder(){
        return new ElementPersistTextInputParser.Builder<T>();
    }

    static <T> ElementBuilder<T> elementBuilder(Class<T> type){
        return new ElementPersistTextInputParser.Builder<T>().dataTypeClass(type);
    }

    static <T> PersistTextInputParser<List<T>> list(@NotNull PersistTextInputParser<T> elementParser, @Nullable PersistentDataType<?, List<T>> dataType){
        return new ListPersistTextInputParser<T>(elementParser, dataType);
    }

    static <T> PersistTextInputParser<List<T>> list(@NotNull PersistTextInputParser<T> elementParser){
        return list(elementParser,  null);
    }

    static <T> PersistTextInputParser<T> primitive(@NotNull PersistentDataType<?, T> dataType, @NotNull Function<Object, T> resultParser){
        return new PrimitivePersistTextInputParser<T>(dataType) {
            @Override
            public @NotNull T decodeObject(@NotNull Object object) throws IllegalArgumentException {
                return resultParser.apply(object);
            }
        };
    }

    PersistTextInputParser<String> STRING = new PrimitivePersistTextInputParser<>(PersistentDataType.STRING) {
        @Override
        public @NotNull String decodeObject(@NotNull Object object) throws IllegalArgumentException {
            return object.toString();
        }
    };

    PersistTextInputParser<Key> KEY = new PrimitivePersistTextInputParser<>(CruxPersistence.CRUX_KEY) {
        @Override
        public @NotNull Key decodeObject(@NotNull Object object) throws IllegalArgumentException {
            return Crux.key(STRING.decodeObject(object));
        }
    };

    PersistTextInputParser<Float> FLOAT = new PrimitivePersistTextInputParser<>(PersistentDataType.FLOAT) {
        @Override
        public @NotNull Float decodeObject(@NotNull Object object) throws IllegalArgumentException {
            return Float.parseFloat(object.toString());
        }
    };

    PersistTextInputParser<Double> DOUBLE = new PrimitivePersistTextInputParser<>(PersistentDataType.DOUBLE) {
        @Override
        public @NotNull Double decodeObject(@NotNull Object object) throws IllegalArgumentException {
            return Double.parseDouble(object.toString());
        }
    };

    PersistTextInputParser<Integer> INTEGER = new PrimitivePersistTextInputParser<>(PersistentDataType.INTEGER) {
        @Override
        public @NotNull Integer decodeObject(@NotNull Object object) throws IllegalArgumentException {
            return Integer.parseInt(object.toString());
        }
    };

    PersistTextInputParser<Boolean> BOOLEAN = new PrimitivePersistTextInputParser<>(PersistentDataType.BOOLEAN) {
        @Override
        public @NotNull Boolean decodeObject(@NotNull Object object) throws IllegalArgumentException {
            String text = object.toString();
            if(text.equalsIgnoreCase("true")) return true;
            if(text.equalsIgnoreCase("false")) return false;
            throw new IllegalArgumentException("Invalid boolean type: " + object);
        }
    };

    ComponentParserListTypeHolder LIST = new ComponentParserListTypeHolder();

    @NotNull
    PersistentDataType<?, T> dataType();

    default @NotNull PersistInputParser<T> createInput(@NotNull Key key){
        return new SimplePersistInputParser<>(key, this);
    }

    interface MapBuilder<T>{
        MapBuilder<T> field(String name, TextInputField<T, ?> field);
        MapBuilder<T> resultParser(TextInputResultParser<T> resultParser);
        PersistTextInputParser<T> apply(TextInputResultParser<T> resultParser);
        MapBuilder<T> dataType(PersistentDataType<PersistentDataContainer, T> dataType);
        MapBuilder<T> dataTypeClass(Class<T> type);
        PersistTextInputParser<T> build();
    }

    interface ElementBuilder<T>{
        ElementBuilder<T> field(TextInputField<T, ?> field);
        ElementBuilder<T> resultParser(TextInputResultParser<T> resultParser);
        PersistTextInputParser<T> apply(TextInputResultParser<T> resultParser);
        ElementBuilder<T> dataType(PersistentDataType<PersistentDataContainer, T> dataType);
        ElementBuilder<T> dataTypeClass(Class<T> type);
        PersistTextInputParser<T> build();
    }
}
