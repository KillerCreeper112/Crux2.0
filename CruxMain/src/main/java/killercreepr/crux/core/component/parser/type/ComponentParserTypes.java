package killercreepr.crux.core.component.parser.type;

import killercreepr.crux.api.component.parser.ComponentTextInputParser;
import killercreepr.crux.core.registries.CruxRegistries;
import org.jetbrains.annotations.NotNull;

public class ComponentParserTypes {
    public static final ComponentTextInputParser<Boolean> BOOLEAN = register(new ComponentTextInputParser<>() {
        @Override
        public @NotNull Boolean parse(@NotNull Object object) throws IllegalArgumentException {
            String x = object.toString();
            if(x.equalsIgnoreCase("true")) return true;
            if(x.equalsIgnoreCase("false")) return false;
            throw new IllegalArgumentException("Invalid boolean type! " + x);
        }
    });

    public static final ComponentTextInputParser<String> STRING = register(new ComponentTextInputParser<>() {
        @Override
        public @NotNull String parse(@NotNull Object object) throws IllegalArgumentException {
            return object.toString();
        }
    });

    public static final ComponentTextInputParser<Integer> INTEGER = register(new ComponentTextInputParser<>() {
        @Override
        public @NotNull Integer parse(@NotNull Object object) throws IllegalArgumentException {
            return Integer.parseInt(object.toString());
        }
    });

    public static final ComponentTextInputParser<Double> DOUBLE = register(new ComponentTextInputParser<>() {
        @Override
        public @NotNull Double parse(@NotNull Object object) throws IllegalArgumentException {
            return Double.parseDouble(object.toString());
        }
    });

    public static final ComponentTextInputParser<Float> FLOAT = register(new ComponentTextInputParser<>() {
        @Override
        public @NotNull Float parse(@NotNull Object object) throws IllegalArgumentException {
            return Float.parseFloat(object.toString());
        }
    });

    public static void register(){}

    public static <T extends ComponentTextInputParser<?>> T register(T value){
        return CruxRegistries.DATA_COMPONENT_TEXT_PARSER_TYPE.register(value);
    }
}
