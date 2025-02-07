package killercreepr.cruxtickables.core.persistence.type;

import killercreepr.crux.core.util.CruxTag;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickable;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickableInstance;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickableModifier;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickablesContainer;
import killercreepr.cruxtickables.core.registries.CruxTickableRegistries;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;

public class EntityTickablesComponentContainerDataType implements PersistentDataType<PersistentDataContainer, EntityTickablesContainer> {
    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<EntityTickablesContainer> getComplexType() {
        return EntityTickablesContainer.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull EntityTickablesContainer complex, @NotNull PersistentDataAdapterContext context) {
        PersistentDataContainer data = context.newPersistentDataContainer();
        complex.getTickableInstances().forEach(instance ->{
            EntityTickable tickable = instance.getTickable();
            instance.getModifiers().forEach(mod ->{
                EntityTickable.addModifierDirect(data, tickable, mod);
            });
        });
        /*complex.getAttributeInstances().forEach(instance ->{
            CruxAttribute attribute = instance.getAttribute();
            instance.getModifiers().forEach(mod ->{
                CruxAttribute.addModifier(data, attribute, mod);
            });
        });*/
        return data;
    }

    static <P extends PersistentDataContainer>
    @Nullable EntityTickableInstance getInstance(@Nullable P i, @NotNull EntityTickable attribute){
        PersistentDataContainer modifierProvider = CruxTag.get(i, attribute.key(), PersistentDataType.TAG_CONTAINER, null);
        if(modifierProvider == null) return null;
        Collection<EntityTickableModifier> list = EntityTickable.parseModifiers(modifierProvider);
        return EntityTickableInstance.instance(attribute, list);
    }

    static <P extends PersistentDataContainer> @NotNull Collection<EntityTickableInstance>
    getInstances(@NotNull P attributeContainer){
        Collection<EntityTickableInstance> list = new HashSet<>();
        for(NamespacedKey k : attributeContainer.getKeys()){
            EntityTickable attribute = CruxTickableRegistries.ENTITY_TICKABLE.get(k);
            if(attribute == null) continue;
            EntityTickableInstance instance = getInstance(attributeContainer, attribute);
            if(instance != null) list.add(instance);
        }
        return list;
    }

    @Override
    public @NotNull EntityTickablesContainer fromPrimitive(@NotNull PersistentDataContainer primitive, @NotNull PersistentDataAdapterContext context) {
        return EntityTickablesContainer.container(getInstances(primitive));
    }
}
