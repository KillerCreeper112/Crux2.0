package killercreepr.crux.core.component.parser.hybrid.text.persistence;

import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.core.component.parser.hybrid.text.DynamicMapPersistTextParser;
import killercreepr.crux.core.util.CruxTag;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DynamicMapDataType<T> implements PersistentDataType<PersistentDataContainer, T> {
    protected final Class<T> complexType;
    protected final Map<String, TextInputField<T, ?>> elements;
    protected final DynamicMapPersistTextParser<T> parser;

    public DynamicMapDataType(Class<T> complexType, Map<String, TextInputField<T, ?>> elements, DynamicMapPersistTextParser<T> parser) {
        this.complexType = complexType;
        this.elements = elements;
        this.parser = parser;
    }

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
        TextInputField<T, ?> base = elements.get("");
        if(base != null){
            PersistTextParser<Object> serializer;
            if(base instanceof TextInputField.Holder<T,?> holder){
                serializer = (PersistTextParser<Object>) holder.getInputParser(complex);
            }else serializer = (PersistTextParser<Object>) base.inputParser();

            return (PersistentDataContainer) serializer.dataType().toPrimitive(complex, context);
        }

        Map<String, Object> map;

        try{
            T test =(T) complex;
            map = parser.encodeObject(complex);
        }catch (ClassCastException ignored){
            map = (Map<String, Object>) complex;
        }
        PersistentDataContainer c = context.newPersistentDataContainer();
        map.forEach((id, value) ->{
            TextInputField<T, ?> field = elements.get(id);
            if(field == null) field = parser.getValueField();
            PersistTextParser<Object> serializer = (PersistTextParser<Object>) field.inputParser();
            CruxTag.set(c, id, serializer.dataType(), value);
        });
        return c;
    }

    @Override
    public @NotNull T fromPrimitive(@NotNull PersistentDataContainer c, @NotNull PersistentDataAdapterContext context) {
        Map<String, Object> map = new HashMap<>();
        for(NamespacedKey key : c.getKeys()){
            String id = key.value();
            TextInputField<T, ?> field = elements.getOrDefault(id, parser.getValueField());
            PersistTextParser<Object> serializer = (PersistTextParser<Object>) field.inputParser();
            Object value = CruxTag.get(c, id, serializer.dataType(), null);
            if(value == null) continue;
            //todo Make better for performance
            map.put(id, serializer.encodeObject(value));
        }

        TextInputField<T, ?> base = elements.get("");
        if(base != null){
            PersistTextParser<Object> serializer;
            if(base instanceof TextInputField.Holder<T,?> holder){
                serializer = (PersistTextParser<Object>) holder.getInputParser(map);
            }else serializer = (PersistTextParser<Object>) base.inputParser();

            return (T) ((PersistentDataType<PersistentDataContainer, ?>)serializer.dataType()).fromPrimitive(c, context);
        }

        return parser.decodeObject(map);
    }
}
