package killercreepr.crux.core.component.parser.hybrid.text.persistence;

import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.core.component.parser.hybrid.text.MapPersistTextParser;
import killercreepr.crux.core.util.CruxTag;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MapDataType<T> implements PersistentDataType<PersistentDataContainer, T> {
    protected final Class<T> complexType;
    protected final Map<String, TextInputField<T, ?>> elements;
    protected final MapPersistTextParser<T> parser;

    public MapDataType(Class<T> complexType, Map<String, TextInputField<T, ?>> elements, MapPersistTextParser<T> parser) {
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
        Map<String, Object> map;

        try{
            T test =(T) complex;
            map = parser.encodeObject(complex);
        }catch (ClassCastException ignored){
            map = (Map<String, Object>) complex;
        }

        PersistentDataContainer c = context.newPersistentDataContainer();
        map.forEach((id, value) ->{
            PersistTextParser<Object> serializer = (PersistTextParser<Object>) elements.get(id).inputParser();
            CruxTag.set(c, id, serializer.dataType(), value);
        });
        return c;
    }

    @Override
    public @NotNull T fromPrimitive(@NotNull PersistentDataContainer c, @NotNull PersistentDataAdapterContext context) {
        Map<String, Object> map = new HashMap<>();
        for(NamespacedKey key : c.getKeys()){
            String id = key.value();
            PersistTextParser<Object> serializer = (PersistTextParser<Object>) elements.get(id).inputParser();
            Object value = CruxTag.get(c, id, serializer.dataType(), null);
            if(value == null) continue;
            //todo Make better for performance
            map.put(id, serializer.encodeObject(value));
        }
        return parser.decodeObject(map);
    }
}
