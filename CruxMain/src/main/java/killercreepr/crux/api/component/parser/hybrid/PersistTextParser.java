package killercreepr.crux.api.component.parser.hybrid;

import killercreepr.crux.api.component.parser.ComponentTextInputParser;
import killercreepr.crux.api.component.parser.standard.ComponentParserListTypeHolder;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.component.parser.hybrid.SimplePersistParser;
import killercreepr.crux.core.component.parser.hybrid.text.*;
import killercreepr.crux.core.persistence.CruxPersistence;
import killercreepr.crux.core.util.CruxColor;
import net.kyori.adventure.key.Key;
import org.bukkit.Color;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface PersistTextParser<T> extends ComponentTextInputParser<T>{
    static <T> MapBuilder<T> mapBuilder(){
        return new MapPersistTextParser.Builder<T>();
    }

    static <T> MapBuilder<T> mapBuilder(Class<T> type){
        return new MapPersistTextParser.Builder<T>().dataTypeClass(type);
    }

    static <T> DynamicMapBuilder<T> mapDynamicBuilder(){
        return new DynamicMapPersistTextParser.Builder<T>();
    }

    static <T> DynamicMapBuilder<T> mapDynamicBuilder(Class<T> type){
        return new DynamicMapPersistTextParser.Builder<T>().dataTypeClass(type);
    }

    static <T> ElementBuilder<T> elementBuilder(){
        return new ElementPersistTextParser.Builder<T>();
    }

    static <T> ElementBuilder<T> elementBuilder(Class<T> type){
        return new ElementPersistTextParser.Builder<T>().dataTypeClass(type);
    }

    static <T> PersistTextParser<List<T>> list(@NotNull PersistTextParser<T> elementParser, @Nullable PersistentDataType<?, List<T>> dataType){
        return new ListPersistTextParser<T>(elementParser, dataType);
    }

    static <T> PersistTextParser<List<T>> list(@NotNull PersistTextParser<T> elementParser){
        return list(elementParser,  null);
    }

    static <T> PersistTextParser<Collection<T>> collectionList(@NotNull PersistTextParser<T> elementParser, @Nullable PersistentDataType<?, Collection<T>> dataType){
        return new CollectionPersistTextParser<>(elementParser, dataType);
    }

    static <T> PersistTextParser<Collection<T>> collectionList(@NotNull PersistTextParser<T> elementParser){
        return collectionList(elementParser,  null);
    }

    static <T> PersistTextParser<T> primitive(@NotNull PersistentDataType<?, T> dataType, @NotNull Function<Object, T> resultParser){
        return new PrimitivePersistTextParser<T>(dataType) {
            @Override
            public @NotNull T decodeObject(@NotNull Object object) throws IllegalArgumentException {
                return resultParser.apply(object);
            }
        };
    }

    PersistTextParser<String> STRING = new PrimitivePersistTextParser<>(PersistentDataType.STRING) {
        @Override
        public @NotNull String decodeObject(@NotNull Object object) throws IllegalArgumentException {
            return object.toString();
        }
    };

    PersistTextParser<byte[]> BYTE_ARRAY = new PrimitivePersistTextParser<>(PersistentDataType.BYTE_ARRAY) {
        @Override
        public byte[] decodeObject(@NotNull Object object) throws IllegalArgumentException {
            return (byte[]) object;
        }
    };

    PersistTextParser<Key> KEY = new PrimitivePersistTextParser<>(CruxPersistence.CRUX_KEY) {
        @Override
        public @NotNull Key decodeObject(@NotNull Object object) throws IllegalArgumentException {
            return Crux.key(STRING.decodeObject(object));
        }
    };

    PersistTextParser<Float> FLOAT = new PrimitivePersistTextParser<>(PersistentDataType.FLOAT) {
        @Override
        public @NotNull Float decodeObject(@NotNull Object object) throws IllegalArgumentException {
            return Float.parseFloat(object.toString());
        }
    };

    PersistTextParser<Double> DOUBLE = new PrimitivePersistTextParser<>(PersistentDataType.DOUBLE) {
        @Override
        public @NotNull Double decodeObject(@NotNull Object object) throws IllegalArgumentException {
            return Double.parseDouble(object.toString());
        }
    };

    PersistTextParser<Long> LONG = new PrimitivePersistTextParser<>(PersistentDataType.LONG) {
        @Override
        public @NotNull Long decodeObject(@NotNull Object object) throws IllegalArgumentException {
            return Long.parseLong(object.toString());
        }
    };

    PersistTextParser<Byte> BYTE = new PrimitivePersistTextParser<>(PersistentDataType.BYTE) {
        @Override
        public @NotNull Byte decodeObject(@NotNull Object object) throws IllegalArgumentException {
            return Byte.parseByte(object.toString());
        }
    };

    PersistTextParser<Integer> INTEGER = new PrimitivePersistTextParser<>(PersistentDataType.INTEGER) {
        @Override
        public @NotNull Integer decodeObject(@NotNull Object object) throws IllegalArgumentException {
            return Integer.parseInt(object.toString());
        }
    };

    PersistTextParser<Boolean> BOOLEAN = new PrimitivePersistTextParser<>(PersistentDataType.BOOLEAN) {
        @Override
        public @NotNull Boolean decodeObject(@NotNull Object object) throws IllegalArgumentException {
            String text = object.toString();
            if(text.equalsIgnoreCase("true")) return true;
            if(text.equalsIgnoreCase("false")) return false;
            throw new IllegalArgumentException("Invalid boolean type: " + object);
        }
    };

    PersistTextParser<Color> COLOR = elementBuilder(Color.class)
        .field(TextInputField.field(STRING, CruxColor::colorToHex))
        .apply(ctx -> CruxColor.hexToColor(ctx.get()));

    ComponentParserListTypeHolder LIST = new ComponentParserListTypeHolder();

    @NotNull
    PersistentDataType<?, T> dataType();

    default @NotNull PersistParser<T> createInput(@NotNull Key key){
        return new SimplePersistParser<>(key, this);
    }

    interface MapBuilder<T>{
        MapBuilder<T> field(String name, TextInputField<T, ?> field);
        MapBuilder<T> resultParser(TextInputResultParser<T> resultParser);
        PersistTextParser<T> apply(TextInputResultParser<T> resultParser);
        MapBuilder<T> dataType(PersistentDataType<PersistentDataContainer, T> dataType);
        MapBuilder<T> dataTypeClass(Class<T> type);
        PersistTextParser<T> build();
        PersistTextParser<T> buildUnset();
        MapBuilder<T> dataTypeFunction(Function<PersistTextParser<T>, PersistentDataType<PersistentDataContainer, T>> function);
    }

    interface DynamicMapBuilder<T> extends MapBuilder<T>{
        DynamicMapBuilder<T> field(String name, TextInputField<T, ?> field);
        DynamicMapBuilder<T> resultParser(TextInputResultParser<T> resultParser);
        PersistTextParser<T> apply(TextInputResultParser<T> resultParser);
        DynamicMapBuilder<T> dataType(PersistentDataType<PersistentDataContainer, T> dataType);
        DynamicMapBuilder<T> dataTypeClass(Class<T> type);
        PersistTextParser<T> build();
        PersistTextParser<T> buildUnset();
        DynamicMapBuilder<T> dataTypeFunction(Function<PersistTextParser<T>, PersistentDataType<PersistentDataContainer, T>> function);
        DynamicMapBuilder<T> keyParser(PersistTextParser<?> field);
        DynamicMapBuilder<T> valueParser(PersistTextParser<?> field);
        DynamicMapBuilder<T> mapToEncode(Function<T, Map<?, ?>> function);
    }

    interface ElementBuilder<T>{
        ElementBuilder<T> field(TextInputField<T, ?> field);
        ElementBuilder<T> resultParser(TextInputResultParser<T> resultParser);
        PersistTextParser<T> apply(TextInputResultParser<T> resultParser);
        ElementBuilder<T> dataType(PersistentDataType<?, T> dataType);
        ElementBuilder<T> dataTypeClass(Class<T> type);
        PersistTextParser<T> build();
    }
}
