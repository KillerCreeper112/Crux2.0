package killercreepr.crux.api.component.parser.hybrid;

import killercreepr.crux.api.component.parser.ComponentTextInputParser;
import killercreepr.crux.core.component.parser.hybrid.SimplePersistInputParser;
import killercreepr.crux.core.component.parser.hybrid.text.PrimitivePersistTextInputParser;
import net.kyori.adventure.key.Key;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public interface PersistTextInputParser<T> extends ComponentTextInputParser<T>{
    PersistTextInputParser<String> STRING = new PrimitivePersistTextInputParser<>(PersistentDataType.STRING) {
        @Override
        public @NotNull String decodeObject(@NotNull Object object) throws IllegalArgumentException {
            return object.toString();
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
}
