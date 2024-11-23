package killercreepr.crux.api.component.parser.hybrid;

import killercreepr.crux.api.component.parser.ComponentTextInputParser;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.component.parser.hybrid.SimplePersistInputParser;
import killercreepr.crux.core.component.parser.hybrid.text.MapPersistTextInputParser;
import killercreepr.crux.core.component.parser.hybrid.text.PrimitivePersistTextInputParser;
import killercreepr.crux.core.persistence.CruxPersistence;
import net.kyori.adventure.key.Key;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public interface PersistTextInputParser<T> extends ComponentTextInputParser<T>{
    static <T> MapBuilder<T> mapBuilder(){
        return new MapPersistTextInputParser.Builder<T>();
    }

    static <T> MapBuilder<T> mapBuilder(Class<T> type){
        return new MapPersistTextInputParser.Builder<T>().dataTypeClass(type);
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
}
