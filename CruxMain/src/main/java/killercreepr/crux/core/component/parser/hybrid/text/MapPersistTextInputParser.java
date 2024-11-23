package killercreepr.crux.core.component.parser.hybrid.text;

import killercreepr.crux.api.component.parser.InputDecodeContext;
import killercreepr.crux.api.component.parser.hybrid.PersistTextInputParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.api.component.parser.hybrid.TextInputResultParser;
import killercreepr.crux.core.util.CruxTag;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MapPersistTextInputParser<T> implements PersistTextInputParser<T> {
    protected final @NotNull Map<String, TextInputField<T, ?>> elements;
    protected final @NotNull TextInputResultParser<T> resultParser;
    protected final @NotNull PersistentDataType<PersistentDataContainer, T> dataType;

    public MapPersistTextInputParser(@NotNull Map<String, TextInputField<T, ?>> elements,
                                     @NotNull TextInputResultParser<T> resultParser,
                                     @NotNull PersistentDataType<PersistentDataContainer, T> dataType) {
        this.elements = elements;
        this.resultParser = resultParser;
        this.dataType = dataType;
    }

    public MapPersistTextInputParser(@NotNull Map<String, TextInputField<T, ?>> elements,
                                     @NotNull TextInputResultParser<T> resultParser,
                                     @NotNull Class<T> type) {
        this.elements = elements;
        this.resultParser = resultParser;
        this.dataType = buildDataType(type, elements);
    }

    public PersistentDataType<PersistentDataContainer, T> buildDataType(@NotNull Class<T> complexType,
                                                                        @NotNull Map<String, TextInputField<T, ?>> elements){
        return new PersistentDataType<>() {
            @Override
            public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
                return PersistentDataContainer.class;
            }

            @Override
            public @NotNull Class<T> getComplexType() {
                return complexType;
            }

            @Override
            public @NotNull PersistentDataContainer toPrimitive(@NotNull T complex, @NotNull PersistentDataAdapterContext context) {
                Map<String, Object> map = encodeObject(complex);
                PersistentDataContainer c = context.newPersistentDataContainer();
                map.forEach((id, value) ->{
                    PersistTextInputParser<Object> serializer = (PersistTextInputParser<Object>) elements.get(id).inputParser();
                    CruxTag.set(c, id, serializer.dataType(), value);
                });
                return c;
            }

            @Override
            public @NotNull T fromPrimitive(@NotNull PersistentDataContainer c, @NotNull PersistentDataAdapterContext context) {
                Map<String, Object> map = new HashMap<>();
                for(NamespacedKey key : c.getKeys()){
                    String id = key.value();
                    PersistTextInputParser<Object> serializer = (PersistTextInputParser<Object>) elements.get(id).inputParser();
                    Object value = CruxTag.get(c, id, serializer.dataType(), null);
                    if(value == null) continue;
                    //todo Make better for performance
                    map.put(id, serializer.encodeObject(value));
                }
                return decodeObject(map);
            }
        };
    }

    @Override
    public @NotNull T decodeObject(@NotNull Object object) throws IllegalArgumentException {
        if(!(object instanceof Map<?,?> map)) throw new IllegalArgumentException(object + " is not Map!");
        Map<String, Object> parsed = new HashMap<>();
        elements.forEach((id, field) ->{
            Object value = map.get(id);
            if(value == null) return;
            Object decoded = field.inputParser().decodeObject(value);
            parsed.put(id, decoded);
        });
        InputDecodeContext ctx = InputDecodeContext.context(parsed);
        return resultParser.parse(ctx);
    }

    @Override
    public @NotNull Map<String, Object> encodeObject(@NotNull T object) {
        Map<String, Object> map = new HashMap<>();
        elements.forEach((id, field) ->{
            Object parsedField = field.parseField(object);
            Object encoded = field.inputParser().encodeObjectUnchecked(parsedField);
            map.put(id, encoded);
        });
        return map;
    }

    /*@Override
    public @Nullable T decode(@NotNull PersistentDataContainer from) {
        return CruxTag.get(from, key, dataType, null);
    }

    @Override
    public void encode(@NotNull PersistentDataContainer to, @Nullable T value) {
        CruxTag.set(to, key, dataType, value);
    }*/

    @Override
    public @NotNull PersistentDataType<?, T> dataType() {
        return dataType;
    }
}
