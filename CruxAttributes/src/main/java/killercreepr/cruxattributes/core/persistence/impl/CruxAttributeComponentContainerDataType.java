package killercreepr.cruxattributes.core.persistence.impl;

import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.attribute.CruxAttributeContainer;
import killercreepr.cruxattributes.api.attribute.CruxAttributeInstance;
import killercreepr.cruxattributes.api.attribute.CruxAttributeModifier;
import killercreepr.cruxattributes.core.registries.CruxAttributeRegistries;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;

public class CruxAttributeComponentContainerDataType implements PersistentDataType<PersistentDataContainer, CruxAttributeContainer> {
    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<CruxAttributeContainer> getComplexType() {
        return CruxAttributeContainer.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull CruxAttributeContainer complex, @NotNull PersistentDataAdapterContext context) {
        PersistentDataContainer data = context.newPersistentDataContainer();
        complex.getAttributeInstances().forEach(instance ->{
            CruxAttribute attribute = instance.getAttribute();
            instance.getModifiers().forEach(mod ->{
                CruxAttribute.addModifier(data, attribute, mod);
            });
        });
        return data;
    }

    static <P extends PersistentDataContainer>
    @Nullable CruxAttributeInstance getInstance(@Nullable P i, @NotNull CruxAttribute attribute){
        PersistentDataContainer modifierProvider = CruxAttribute.getAttributeContainerFromBase(i, attribute);
        if(modifierProvider == null) return null;
        Collection<CruxAttributeModifier> list = CruxAttribute.convertToModifiers(modifierProvider);
        return CruxAttributeInstance.instance(attribute, list);
    }

    static <P extends PersistentDataContainer> @NotNull Collection<CruxAttributeInstance>
    getInstances(@NotNull P attributeContainer){
        Collection<CruxAttributeInstance> list = new HashSet<>();
        for(NamespacedKey k : attributeContainer.getKeys()){
            CruxAttribute attribute = CruxAttributeRegistries.ATTRIBUTES.get(k);
            if(attribute == null) continue;
            CruxAttributeInstance instance = getInstance(attributeContainer, attribute);
            if(instance != null) list.add(instance);
        }
        return list;
    }

    @Override
    public @NotNull CruxAttributeContainer fromPrimitive(@NotNull PersistentDataContainer primitive, @NotNull PersistentDataAdapterContext context) {
        return CruxAttributeContainer.container(getInstances(primitive));
    }
}
