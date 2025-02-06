package killercreepr.cruxattributes.bukkit;

import killercreepr.cruxattributes.api.equipment.CruxSlot;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

public class AttributeBukkitAdaptor {
    public static @Nullable EquipmentSlot adapt(CruxSlot slot){
        try{
            if(slot.equals(CruxSlot.MAIN_HAND)) return EquipmentSlot.HAND;
            return EquipmentSlot.valueOf(slot.key().value().toUpperCase());
        }catch (IllegalArgumentException ignored){ return null; }
    }
    public static @Nullable CruxSlot adapt(EquipmentSlot slot){
        return switch (slot){
            case HEAD -> CruxSlot.HEAD;
            case CHEST -> CruxSlot.CHEST;
            case LEGS -> CruxSlot.LEGS;
            case FEET -> CruxSlot.FEET;
            case HAND -> CruxSlot.MAIN_HAND;
            case OFF_HAND -> CruxSlot.OFF_HAND;
            default -> null;
        };
    }
}
