package killercreepr.cruxattributes.api.equipment;

import killercreepr.crux.api.data.CruxKeyed;
import killercreepr.crux.api.translation.Translatable;
import killercreepr.crux.core.Crux;
import killercreepr.cruxattributes.core.equipment.MainHandSlot;
import killercreepr.cruxattributes.core.equipment.SimpleCruxSlot;
import killercreepr.cruxattributes.core.registries.CruxAttributeRegistries;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface CruxSlot extends CruxKeyed, Translatable {
    static void register(){}

    CruxSlot MAIN_HAND = register(new MainHandSlot(Crux.key("main_hand")));
    CruxSlot OFF_HAND = register(new SimpleCruxSlot(Crux.key("off_hand"), 40));
    CruxSlot HEAD = register(new SimpleCruxSlot(Crux.key("head"), 39));
    CruxSlot CHEST = register(new SimpleCruxSlot(Crux.key("chest"), 38));
    CruxSlot LEGS = register(new SimpleCruxSlot(Crux.key("legs"), 37));
    CruxSlot FEET = register(new SimpleCruxSlot(Crux.key("feet"), 36));

    private static CruxSlot register(CruxSlot slot){
        return CruxAttributeRegistries.SLOT.register(slot);
    }

    static CruxSlot getActivationSlot(Entity user, int slot){
        for(CruxSlot crux : CruxAttributeRegistries.SLOT){
            if(crux.wouldActivate(user, slot)) return crux;
        }
        return null;
    }

    boolean wouldActivate(@NotNull Entity p, int slot);
    int getIndex(@NotNull Entity p);

    /*public @NotNull Key key(){
        return CruxAttribute.k("slot." + this.toString().toLowerCase());
    }*/
}
