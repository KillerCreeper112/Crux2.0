package killercreepr.cruxattributes.core.persistence.impl;

import killercreepr.crux.util.CruxKey;
import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.attribute.CruxAttributeInstance;
import killercreepr.cruxattributes.api.attribute.CruxAttributeModifier;
import killercreepr.cruxattributes.core.attribute.SimpleCruxAttributeInstance;
import killercreepr.cruxattributes.core.attribute.SimpleCruxAttributeModifier;
import killercreepr.cruxattributes.core.persistence.CruxAttributesPersistence;
import killercreepr.cruxattributes.core.registries.CruxAttributeRegistries;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class CruxAttributeContainerDataType implements PersistentDataType<PersistentDataContainer, Collection<CruxAttributeInstance>> {
    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<Collection<CruxAttributeInstance>> getComplexType() {
        return (Class<Collection<CruxAttributeInstance>>) (Class<?>) Collection.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull Collection<CruxAttributeInstance> complex, @NotNull PersistentDataAdapterContext context) {
        PersistentDataContainer c = context.newPersistentDataContainer();
        for(CruxAttributeInstance m : complex){
            c.set(CruxKey.key(m.getAttribute().key()), CruxAttributesPersistence.ATTRIBUTE_INSTANCE, m.getModifiers());
        }
        return c;
    }

    @Override
    public @NotNull Collection<CruxAttributeInstance> fromPrimitive(@NotNull PersistentDataContainer c, @NotNull PersistentDataAdapterContext context) {
        Collection<CruxAttributeInstance> list = new ArrayList<>();
        for(NamespacedKey k : c.getKeys()){
            try{
                CruxAttribute attribute = CruxAttributeRegistries.ATTRIBUTES.get(k);
                if(attribute == null) continue;
                Collection<CruxAttributeModifier> modifiers = c.get(k, CruxAttributesPersistence.ATTRIBUTE_INSTANCE);
                if(modifiers != null) list.add(CruxAttributeInstance.instance(attribute, modifiers));
            }catch (Exception ignored){}
        }
        return list;
    }
}
