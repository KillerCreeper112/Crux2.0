package killercreepr.cruxmenus.core.menu.data;

import killercreepr.crux.api.component.parser.ComponentTextInputParser;
import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.core.component.parser.TextDataComponentDecoder;

import java.util.HashMap;
import java.util.Map;

//todo WORK ON THIS TEMP SETUP
public class MenuInfoDataParser {
    public static Map<String, ?> parse(String input){
        Map<String, Object> map = new HashMap<>();
        plainParse(input).forEach((id, value) ->{
            if(!(value instanceof Map<?, ?> valueMap)){
                map.put(id, value);
                return;
            }
            map.put(id, parseValue(valueMap));
        });
        return map;
    }

    public static final Map<String, ComponentTextInputParser<?>> parsers = Map.of(
        "color", PersistTextParser.COLOR
    );
    public static Object parseValue(Map<?, ?> map){
        Object parserID = map.get("parser");
        if(parserID==null) return map;
        ComponentTextInputParser<?> parser = parsers.get(parserID.toString());
        return parser.decodeObject(map.get("value"));
    }

    public static Map<String, ?> plainParse(String input){
        return TextDataComponentDecoder.parseComponentMap(input);
    }
}
