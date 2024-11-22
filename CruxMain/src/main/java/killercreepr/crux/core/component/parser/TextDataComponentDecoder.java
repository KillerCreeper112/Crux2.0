package killercreepr.crux.core.component.parser;

import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.crux.api.component.parser.DataComponentDecoder;
import killercreepr.crux.api.component.parser.ComponentTextInputParser;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registries.CruxRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.logging.Level;

public class TextDataComponentDecoder implements DataComponentDecoder {
    @Override
    public @NotNull Collection<TypedDataComponent<?>> parseComponents(@NotNull String input){
        Collection<TypedDataComponent<?>> list = new HashSet<>();
        Map<String, Object> map = parseComponentMap(input);
        map.forEach((id, value) ->{
            DataComponentType<?> type = CruxRegistries.DATA_COMPONENT_TYPE.get(Crux.key(id));
            if(type == null){
                Crux.log(Level.SEVERE, "DataComponentType of " + id + " not found!");
                return;
            }
            ComponentTextInputParser<?> parser = type.textParser();
            if(parser == null){
                Crux.log(Level.SEVERE, "DataComponentType of " + id + " is not text-input parsable!");
                return;
            }
            try{
                Object result = parser.decodeObject(value);
                TypedDataComponent<?> typed = TypedDataComponent.createUnchecked(type, result);
                list.add(typed);
            }catch (IllegalArgumentException e){
                Crux.log(Level.SEVERE, "Invalid value for DataComponentType," + id + "! " + value);
                e.printStackTrace();
            }
        });
        return list;
    }

    public static Map<String, Object> parseComponentMap(String input) {
        // Remove the surrounding brackets and trim any whitespace
        String componentsString = input.trim();
        if (componentsString.startsWith("[") && componentsString.endsWith("]")) {
            componentsString = componentsString.substring(1, componentsString.length() - 1).trim();
        }
        if(componentsString.isEmpty()) return Map.of();

        // Split the components using a custom method that handles nested structures
        String[] propertyPairs = customSplit(componentsString);

        Map<String, Object> components = new HashMap<>();

        // Process each property pair
        for (String pair : propertyPairs) {
            // Skip empty or malformed pairs
            if (pair.trim().isEmpty()) {
                continue;  // You can log a warning if needed
            }

            String[] keyValue = pair.split("=", 2); // Split only on the first '='
            if (keyValue.length < 2) {
                continue;  // Skip malformed pairs
            }

            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            components.put(key, parseObject(value));
        }

        return components;
    }

    // Custom split method to handle nested structures
    private static String[] customSplit(String input) {
        StringBuilder currentProperty = new StringBuilder();
        int openBraces = 0; // Keeps track of the level of nested curly braces
        int openBrackets = 0;
        //StringBuilder result = new StringBuilder();
        List<String> result = new ArrayList<>();

        // Traverse through the input string and split appropriately
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            if (ch == '{') {
                openBraces++;  // Increase the nested structure level when encountering '{'
            } else if (ch == '}') {
                openBraces--;  // Decrease the nested structure level when encountering '}'
            } else if (ch == '[') {
                openBrackets++;  // Increase the nested structure level when encountering '{'
            } else if (ch == ']') {
                openBrackets--;  // Decrease the nested structure level when encountering '}'
            } else if (ch == ',' && openBraces == 0 && openBrackets == 0) {
                // Split only when the comma is outside of any nested structure
                if(!currentProperty.isEmpty()) result.add(currentProperty.toString());
                //result.append(currentProperty).append(',');
                currentProperty.setLength(0); // Reset for the next property
                continue;
            }

            currentProperty.append(ch); // Add the current character to the property
        }

        // Add the last property (without trailing comma)
        if(!currentProperty.isEmpty()) result.add(currentProperty.toString());
        return result.toArray(new String[0]);
    }

    private static Object parseObject(String value){
        if(value.startsWith("{")){
            return parseNestedProperties(value);
        }
        if(value.startsWith("[")){
            return parseListProperties(value);
        }
        return value;
    }

    private static List<Object> parseListProperties(String nestedProperties){
        List<Object> map = new ArrayList<>();
        nestedProperties = nestedProperties.substring(1, nestedProperties.length() - 1).trim();
        if(nestedProperties.isEmpty()) return map;

        // Split by commas to handle key-value pairs inside the curly braces
        String[] pairs = customSplit(nestedProperties);
        for (String pair : pairs) {
            map.add(parseObject(pair));
        }

        return map;
    }

    // Parse nested key-value pairs like {sharpness:1,fortune:3}
    private static Map<String, Object> parseNestedProperties(String nestedProperties) {
        Map<String, Object> map = new HashMap<>();
        nestedProperties = nestedProperties.substring(1, nestedProperties.length() - 1).trim(); // Remove the curly braces

        // Split by commas to handle key-value pairs inside the curly braces
        String[] pairs = customSplit(nestedProperties);
        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2); // Split only on the first ':'
            if(keyValue.length < 2){
                Crux.log(Level.SEVERE, "Cannot parse nested property: " + pair + ", keyValue=" + Arrays.toString(keyValue));
                continue;
            }
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            map.put(key, parseObject(value));
        }

        return map;
    }
}
