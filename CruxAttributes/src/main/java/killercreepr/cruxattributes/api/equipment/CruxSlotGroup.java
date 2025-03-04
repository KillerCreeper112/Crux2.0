package killercreepr.cruxattributes.api.equipment;

import killercreepr.crux.api.data.CruxKeyed;
import killercreepr.crux.api.translation.Translatable;
import killercreepr.crux.core.Crux;
import killercreepr.cruxattributes.core.equipment.SimpleCruxSlotGroup;
import killercreepr.cruxattributes.core.registries.CruxAttributeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface CruxSlotGroup extends Comparable<CruxSlotGroup>, CruxKeyed, Translatable, Iterable<CruxSlot> {
    static void register(){}

    CruxSlotGroup ANY = register(new SimpleCruxSlotGroup(Crux.key("any"), e -> true, "When equipped"));
    CruxSlotGroup HAND = register(new SimpleCruxSlotGroup(Crux.key("hand"), "When in Hand", CruxSlot.MAIN_HAND, CruxSlot.OFF_HAND));
    CruxSlotGroup MAIN_HAND = register(new SimpleCruxSlotGroup(Crux.key("main_hand"), "When in Main Hand", CruxSlot.MAIN_HAND));
    CruxSlotGroup OFF_HAND = register(new SimpleCruxSlotGroup(Crux.key("off_hand"),"When in Off Hand", CruxSlot.OFF_HAND));
    CruxSlotGroup HEAD = register(new SimpleCruxSlotGroup(Crux.key("head"), "When on Head", CruxSlot.HEAD));
    CruxSlotGroup CHEST = register(new SimpleCruxSlotGroup(Crux.key("chest"), "When on Body", CruxSlot.CHEST));
    CruxSlotGroup LEGS = register(new SimpleCruxSlotGroup(Crux.key("legs"),"When on Legs", CruxSlot.LEGS));
    CruxSlotGroup FEET = register(new SimpleCruxSlotGroup(Crux.key("feet"), "When on Feet", CruxSlot.FEET));
    CruxSlotGroup ARMOR = register(new SimpleCruxSlotGroup(Crux.key("armor"), "When worn", CruxSlot.HEAD, CruxSlot.CHEST, CruxSlot.LEGS, CruxSlot.FEET));

    private static CruxSlotGroup register(CruxSlotGroup group){
        return CruxAttributeRegistries.SLOT_GROUP.register(group);
    }

    String getWhenInSlot();

    boolean test(@NotNull CruxSlot slot);
    default boolean anyMatch(@NotNull CruxSlot... slots){
        for(CruxSlot s : slots){
            if(test(s)) return true;
        }
        return false;
    }
    default boolean allMatch(@NotNull CruxSlot... slots){
        for(CruxSlot s : slots){
            if(!test(s)) return false;
        }
        return true;
    }
    default boolean anyMatch(@NotNull Collection<CruxSlot> slots){
        for(CruxSlot s : slots){
            if(test(s)) return true;
        }
        return false;
    }
    default boolean allMatch(@NotNull Collection<CruxSlot> slots){
        for(CruxSlot s : slots){
            if(!test(s)) return false;
        }
        return true;
    }
}
