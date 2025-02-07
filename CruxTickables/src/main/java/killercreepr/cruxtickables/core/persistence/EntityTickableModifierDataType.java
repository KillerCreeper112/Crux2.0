package killercreepr.cruxtickables.core.persistence;

import killercreepr.crux.api.component.parser.hybrid.PersistParser;
import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.core.component.parser.hybrid.text.MapPersistTextParser;
import killercreepr.crux.core.component.parser.hybrid.text.persistence.MapDataType;
import killercreepr.crux.core.util.CruxTag;
import killercreepr.cruxtickables.api.entity.tickable.DataEntityTickable;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickableModifier;
import killercreepr.cruxtickables.core.entity.tickable.SimpleEntityTickableModifier;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class EntityTickableModifierDataType extends MapDataType<EntityTickableModifier> {
    public EntityTickableModifierDataType(Class<EntityTickableModifier> complexType, Map<String, TextInputField<EntityTickableModifier, ?>> elements, MapPersistTextParser<EntityTickableModifier> parser) {
        super(complexType, elements, parser);
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull EntityTickableModifier complex, @NotNull PersistentDataAdapterContext context) {
        TextInputField<EntityTickableModifier, ?> base = elements.get("");
        if(base != null){
            PersistTextParser<Object> serializer;
            if(base instanceof TextInputField.Holder<EntityTickableModifier,?> holder){
                serializer = (PersistTextParser<Object>) holder.getInputParser(complex);
            }else serializer = (PersistTextParser<Object>) base.inputParser();

            return (PersistentDataContainer) serializer.dataType().toPrimitive(complex, context);
        }

        boolean setData = false;
        Map<String, Object> map;
        PersistentDataContainer c = context.newPersistentDataContainer();
        try{
            EntityTickableModifier test =(EntityTickableModifier) complex;
            map = parser.encodeObject(complex);

            if(test.getData() != null && complex.getTickable() instanceof DataEntityTickable dataTickable){
                ((PersistParser)dataTickable.getDataParser()).encode(c, test.getData());
                setData = true;
                /*CruxTag.set(dataContainer, "data", dataTickable.getDataParser().dataType(), );
                map.put("data", dataTickable.getDataParser().encodeObjectUnchecked(test.getData()));*/
            }

        }catch (ClassCastException ignored){
            map = (Map<String, Object>) complex;
        }
        if(map.containsKey("data") && !setData){
            if(complex.getTickable() instanceof DataEntityTickable dataTickable){
                ((PersistParser)dataTickable.getDataParser()).encode(c, map.get("data"));
            }
        }

        map.forEach((id, value) ->{
            TextInputField<EntityTickableModifier, ?> field = elements.get(id);
            if(field==null || id.equalsIgnoreCase("data")) return;
            PersistTextParser<Object> serializer = (PersistTextParser<Object>) field.inputParser();
            CruxTag.set(c, id, serializer.dataType(), value);
        });
        return c;
    }

    @Override
    public @NotNull EntityTickableModifier fromPrimitive(@NotNull PersistentDataContainer c, @NotNull PersistentDataAdapterContext context) {
        Map<String, Object> map = new HashMap<>();
        PersistentDataContainer dataContainer = null;
        for(NamespacedKey key : c.getKeys()){
            String id = key.value();
            if(id.equalsIgnoreCase("data")){
                dataContainer = CruxTag.get(c, key, PersistentDataType.TAG_CONTAINER, null);
                continue;
            }
            if(!elements.containsKey(id)){
                continue;
            }
            PersistTextParser<Object> serializer = (PersistTextParser<Object>) elements.get(id).inputParser();
            Object value = CruxTag.get(c, id, serializer.dataType(), null);
            if(value == null) continue;
            //todo Make better for performance
            map.put(id, serializer.encodeObject(value));
        }

        TextInputField<EntityTickableModifier, ?> base = elements.get("");
        if(base != null){
            PersistTextParser<Object> serializer;
            if(base instanceof TextInputField.Holder<EntityTickableModifier,?> holder){
                serializer = (PersistTextParser<Object>) holder.getInputParser(map);
            }else serializer = (PersistTextParser<Object>) base.inputParser();

            return (EntityTickableModifier) ((PersistentDataType<PersistentDataContainer, ?>)serializer.dataType()).fromPrimitive(c, context);
        }
        EntityTickableModifier mod = parser.decodeObject(map);
        if(dataContainer==null) return mod;
        if(!(mod.getTickable() instanceof DataEntityTickable dataTickable)) return mod;
        Object data = dataTickable.getDataParser().decode(c);
        mod = new SimpleEntityTickableModifier(mod.key(), mod.getTickable(), mod.getSlotGroup(), data);
        return mod;
    }
}
