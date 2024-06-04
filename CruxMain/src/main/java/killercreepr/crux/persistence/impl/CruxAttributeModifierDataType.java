package killercreepr.crux.persistence.impl;

import killercreepr.crux.attribute.CruxAttribute;
import killercreepr.crux.attribute.CruxSlot;
import killercreepr.crux.attribute.container.CruxAttributeModData;
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
        if(complex.getSlot() != null) c.set(CruxAttribute.k("slot"), PersistentDataType.STRING, complex.getSlot().toString().toLowerCase());
        return c;
    }

    @Override
    public @NotNull CruxAttributeModData fromPrimitive(@NotNull PersistentDataContainer c, @NotNull PersistentDataAdapterContext context) {
        try{
            CruxSlot slot;
            if(c.has(CruxAttribute.k("slot"))){
                slot = CruxSlot.valueOf(c.getOrDefault(CruxAttribute.k("slot"), PersistentDataType.STRING, ""));
            }else slot = null;
            return new CruxAttributeModData(
                    c.getOrDefault(CruxAttribute.k("value"), PersistentDataType.DOUBLE, 0D),
                    CruxAttribute.Operation.valueOf(c.getOrDefault(CruxAttribute.k("operation"), PersistentDataType.STRING, "")),
                    slot
            );
        }catch (Exception ex){
            throw new RuntimeException("Cannot convert PersistentDataContainer into GrimAttributeModifier!");
        }
    }
}
