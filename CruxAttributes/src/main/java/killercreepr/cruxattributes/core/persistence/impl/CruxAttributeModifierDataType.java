package killercreepr.cruxattributes.core.persistence.impl;

import killercreepr.crux.core.persistence.CruxPersistence;
import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxattributes.core.attribute.container.CruxAttributeModData;
import killercreepr.cruxattributes.core.registries.CruxAttributeRegistries;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class CruxAttributeModifierDataType implements PersistentDataType<PersistentDataContainer, CruxAttributeModData> {
    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<CruxAttributeModData> getComplexType() {
        return CruxAttributeModData.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull CruxAttributeModData complex, @NotNull PersistentDataAdapterContext context) {
        PersistentDataContainer c = context.newPersistentDataContainer();
        c.set(CruxAttribute.k("value"), PersistentDataType.DOUBLE, complex.getAmount());
        c.set(CruxAttribute.k("operation"), PersistentDataType.STRING, complex.getOperation().toString().toLowerCase());
        if(complex.getSlotGroup() != null){
            c.set(CruxAttribute.k("slot"), CruxPersistence.CRUX_KEY, complex.getSlotGroup().key());
        }
        return c;
    }

    @Override
    public @NotNull CruxAttributeModData fromPrimitive(@NotNull PersistentDataContainer c, @NotNull PersistentDataAdapterContext context) {
        try{
            CruxSlotGroup slot;
            if(c.has(CruxAttribute.k("slot"))){
                slot = CruxAttributeRegistries.SLOT_GROUP.get(c.get(CruxAttribute.k("slot"), CruxPersistence.CRUX_KEY));
            }else slot = null;
            return new CruxAttributeModData(
                    c.getOrDefault(CruxAttribute.k("value"), PersistentDataType.DOUBLE, 0D),
                    CruxAttribute.Operation.valueOf(c.getOrDefault(CruxAttribute.k("operation"), PersistentDataType.STRING, "")),
                    slot
            );
        }catch (Exception ex){
            throw new RuntimeException("Cannot convert PersistentDataContainer into CruxAttributeModifier!");
        }
    }
}
