package killercreepr.cruxattributes.persistence.impl;

import killercreepr.crux.util.CruxKey;
import killercreepr.cruxattributes.attribute.CruxAttributeModifier;
import killercreepr.cruxattributes.attribute.container.CruxAttributeModData;
import killercreepr.cruxattributes.persistence.CruxAttributesPersistence;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class CruxAttributeInstanceDataType implements PersistentDataType<PersistentDataContainer, Collection<CruxAttributeModifier>> {
    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<Collection<CruxAttributeModifier>> getComplexType() {
        return (Class<Collection<CruxAttributeModifier>>) (Class<?>) Collection.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull Collection<CruxAttributeModifier> complex, @NotNull PersistentDataAdapterContext context) {
        PersistentDataContainer c = context.newPersistentDataContainer();
        for(CruxAttributeModifier m : complex){
            c.set(CruxKey.key(m.key()), CruxAttributesPersistence.ATTRIBUTE_MODIFIER, new CruxAttributeModData(m));
        }
        return c;
    }

    @Override
    public @NotNull Collection<CruxAttributeModifier> fromPrimitive(@NotNull PersistentDataContainer c, @NotNull PersistentDataAdapterContext context) {
        Collection<CruxAttributeModifier> list = new ArrayList<>();
        for(NamespacedKey k : c.getKeys()){
            try{
                CruxAttributeModData data = c.get(k, CruxAttributesPersistence.ATTRIBUTE_MODIFIER);
                if(data != null) list.add(new CruxAttributeModifier(k, data));
            }catch (Exception ignored){}
        }
        return list;
    }
}
