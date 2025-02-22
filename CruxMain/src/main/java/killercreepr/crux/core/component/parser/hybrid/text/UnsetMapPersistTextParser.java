package killercreepr.crux.core.component.parser.hybrid.text;

import killercreepr.crux.api.component.parser.InputDecodeContext;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.api.component.parser.hybrid.TextInputResultParser;
import killercreepr.crux.core.component.parser.hybrid.text.persistence.MapDataType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Deprecated(since = "Not sure where to go with this.")
public class UnsetMapPersistTextParser<T> extends MapPersistTextParser<T> {
    public UnsetMapPersistTextParser(@NotNull Map<String, TextInputField<T, ?>> elements, @NotNull TextInputResultParser<T> resultParser, @NotNull PersistentDataType<PersistentDataContainer, T> dataType) {
        super(elements, resultParser, dataType);
    }

    public UnsetMapPersistTextParser(@NotNull Map<String, TextInputField<T, ?>> elements, @NotNull TextInputResultParser<T> resultParser, @NotNull Class<T> type) {
        super(elements, resultParser, type);
    }

    public PersistentDataType<PersistentDataContainer, T> buildDataType(@NotNull Class<T> complexType,
                                                                        @NotNull Map<String, TextInputField<T, ?>> elements){
        return new MapDataType<>(complexType, elements, this);
    }

    @Override
    public @NotNull T decodeObject(@NotNull Object object) throws IllegalArgumentException {
        if(!(object instanceof Map<?,?> map)) throw new IllegalArgumentException(object + " is not Map!");
        TextInputField<T,?> base = elements.getOrDefault("", elements.get(null));
        if(base != null){
            if(base instanceof TextInputField.Holder<T,?> holder){
                return (T) holder.getInputParser(object).decodeObject(object);
            }
            return (T) base.inputParser().decodeObject(object);
        }
        Map<String, Object> parsed = new HashMap<>();
        map.forEach((id, value) ->{
            TextInputField<?, ?> field = elements.get(id.toString());
            Object decoded;
            if(field == null) decoded = value;
            else decoded = field.inputParser().decodeObject(value);
            parsed.put(id.toString(), decoded);
        });
        InputDecodeContext ctx = InputDecodeContext.context(parsed);
        return resultParser.parse(ctx);

        /*Map<String, Object> parsed = new HashMap<>();
        elements.forEach((id, field) ->{
            if(id == null || id.isBlank()){
                return;
            }
            Object value = map.get(id);
            if(value == null) return;
            Object decoded = field.inputParser().decodeObject(value);
            parsed.put(id, decoded);
        });
        InputDecodeContext ctx = InputDecodeContext.context(parsed);
        return resultParser.parse(ctx);*/
    }
}
