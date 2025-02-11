package killercreepr.cruxattributes.bukkit;

import killercreepr.cruxattributes.api.equipment.CruxSlot;
import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.Nullable;

public class AttributeBukkitAdaptor {
    public static EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{
        EquipmentSlot.HEAD,
        EquipmentSlot.CHEST,
        EquipmentSlot.LEGS,
        EquipmentSlot.FEET
    };
    public static EquipmentSlot[] HAND_SLOTS = new EquipmentSlot[]{
        EquipmentSlot.HAND,
        EquipmentSlot.OFF_HAND
    };

    public static @Nullable CruxSlotGroup adapt(EquipmentSlotGroup slot) {
        if (slot == EquipmentSlotGroup.ANY) return CruxSlotGroup.ANY;
        if (slot == EquipmentSlotGroup.HEAD) return CruxSlotGroup.HEAD;
        if (slot == EquipmentSlotGroup.CHEST) return CruxSlotGroup.CHEST;
        if (slot == EquipmentSlotGroup.LEGS) return CruxSlotGroup.LEGS;
        if (slot == EquipmentSlotGroup.FEET) return CruxSlotGroup.FEET;
        if (slot == EquipmentSlotGroup.HAND) return CruxSlotGroup.HAND;
        if (slot == EquipmentSlotGroup.MAINHAND) return CruxSlotGroup.MAIN_HAND;
        if (slot == EquipmentSlotGroup.OFFHAND) return CruxSlotGroup.OFF_HAND;
        if (slot == EquipmentSlotGroup.ARMOR) return CruxSlotGroup.ARMOR;
        return null;
    }

    public static @Nullable EquipmentSlotGroup adapt(CruxSlotGroup slot) {
        if(slot == CruxSlotGroup.ANY) return EquipmentSlotGroup.ANY;
        if(slot == CruxSlotGroup.HEAD) return EquipmentSlotGroup.HEAD;
        if(slot == CruxSlotGroup.CHEST) return EquipmentSlotGroup.CHEST;
        if(slot == CruxSlotGroup.LEGS) return EquipmentSlotGroup.LEGS;
        if(slot == CruxSlotGroup.FEET) return EquipmentSlotGroup.FEET;
        if(slot == CruxSlotGroup.HAND) return EquipmentSlotGroup.HAND;
        if(slot == CruxSlotGroup.MAIN_HAND) return EquipmentSlotGroup.MAINHAND;
        if(slot == CruxSlotGroup.OFF_HAND) return EquipmentSlotGroup.OFFHAND;
        if(slot == CruxSlotGroup.ARMOR) return EquipmentSlotGroup.ARMOR;
        return null;
    }

    public static @Nullable EquipmentSlot adapt(CruxSlot slot) {
        try {
            if (slot.equals(CruxSlot.MAIN_HAND)) return EquipmentSlot.HAND;
            return EquipmentSlot.valueOf(slot.key().value().toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    public static @Nullable CruxSlot adapt(EquipmentSlot slot) {
        return switch (slot) {
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
