package killercreepr.cruxattributes.api.equipment;

import killercreepr.crux.api.data.CruxKeyed;
import killercreepr.crux.core.Crux;
import killercreepr.cruxattributes.core.equipment.SimpleCruxSlotGroup;
import killercreepr.cruxattributes.core.registries.CruxAttributeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface CruxSlotGroup extends CruxKeyed, Iterable<CruxSlot> {
    static void register(){}

    CruxSlotGroup ANY = register(new SimpleCruxSlotGroup(Crux.key("any"), e -> true));
    CruxSlotGroup HAND = register(new SimpleCruxSlotGroup(Crux.key("hand"), CruxSlot.MAIN_HAND, CruxSlot.OFF_HAND));
    CruxSlotGroup MAIN_HAND = register(new SimpleCruxSlotGroup(Crux.key("main_hand"), CruxSlot.MAIN_HAND));
    CruxSlotGroup OFF_HAND = register(new SimpleCruxSlotGroup(Crux.key("off_hand"), CruxSlot.OFF_HAND));
    CruxSlotGroup HEAD = register(new SimpleCruxSlotGroup(Crux.key("head"), CruxSlot.HEAD));
    CruxSlotGroup CHEST = register(new SimpleCruxSlotGroup(Crux.key("chest"), CruxSlot.CHEST));
    CruxSlotGroup LEGS = register(new SimpleCruxSlotGroup(Crux.key("legs"), CruxSlot.LEGS));
    CruxSlotGroup FEET = register(new SimpleCruxSlotGroup(Crux.key("feet"), CruxSlot.FEET));
    CruxSlotGroup ARMOR = register(new SimpleCruxSlotGroup(Crux.key("armor"), CruxSlot.HEAD, CruxSlot.CHEST, CruxSlot.LEGS, CruxSlot.FEET));

    private static CruxSlotGroup register(CruxSlotGroup group){
        return CruxAttributeRegistries.SLOT_GROUP.register(group);
    }

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
