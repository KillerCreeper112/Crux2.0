package killercreepr.crux.core.component.parser.type;

import killercreepr.crux.api.component.parser.ComponentTextInputParser;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class ComponentParserTypes {
    public static final ComponentTextInputParser<Boolean> BOOLEAN = register(Boolean.class, new ComponentTextInputParser<>() {
        @Override
        public @NotNull Boolean decodeObject(@NotNull Object object) throws IllegalArgumentException {
            String x = object.toString();
            if(x.equalsIgnoreCase("true")) return true;
            if(x.equalsIgnoreCase("false")) return false;
            throw new IllegalArgumentException("Invalid boolean type! " + x);
        }
    });

    public static final ComponentTextInputParser<String> STRING = register(String.class, new ComponentTextInputParser<>() {
        @Override
        public @NotNull String decodeObject(@NotNull Object object) throws IllegalArgumentException {
            return object.toString();
        }
    });

    public static final ComponentTextInputParser<Key> KEY = register(String.class, new ComponentTextInputParser<>() {
        @Override
        public @NotNull Key decodeObject(@NotNull Object object) throws IllegalArgumentException {
            return Crux.key(object.toString());
        }
    });

    public static final ComponentTextInputParser<Integer> INTEGER = register(Integer.class, new ComponentTextInputParser<>() {
        @Override
        public @NotNull Integer decodeObject(@NotNull Object object) throws IllegalArgumentException {
            return Integer.parseInt(object.toString());
        }
    });

    public static final ComponentTextInputParser<Double> DOUBLE = register(Double.class, new ComponentTextInputParser<>() {
        @Override
        public @NotNull Double decodeObject(@NotNull Object object) throws IllegalArgumentException {
            return Double.parseDouble(object.toString());
        }
    });

    public static final ComponentTextInputParser<Float> FLOAT = register(Float.class, new ComponentTextInputParser<>() {
        @Override
        public @NotNull Float decodeObject(@NotNull Object object) throws IllegalArgumentException {
            return Float.parseFloat(object.toString());
        }
    });

    public static void register(){}

    public static <T extends ComponentTextInputParser<?>> T register(Class<?> type, T value){
        return CruxRegistries.DATA_COMPONENT_TEXT_PARSER_TYPE.register(type, value);
    }

    public static final ComponentParserListTypeHolder LIST = new ComponentParserListTypeHolder();
}
