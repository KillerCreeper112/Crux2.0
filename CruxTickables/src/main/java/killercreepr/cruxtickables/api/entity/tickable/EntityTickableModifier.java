package killercreepr.cruxtickables.api.entity.tickable;

import killercreepr.crux.api.data.CruxKeyed;
import killercreepr.crux.core.Crux;
import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxtickables.core.entity.tickable.SimpleEntityTickableModifier;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface EntityTickableModifier extends CruxKeyed {
    /*static EntityTickableModifier modifier(Key key, EntityTickable tickable, CruxSlotGroup slot, Key... path){
        return new SimpleEntityTickableModifier(key, slot, path);
    }
    static EntityTickableModifier modifier(Key key, EntityTickable tickable, CruxSlotGroup slot){
        return modifier(key, slot, (Key[]) null);
    }
    static EntityTickableModifier modifier(Key key){
        return modifier(key, null);
    }
    static EntityTickableModifier baseModifier(){
        return modifier(Crux.key("base"), null);
    }
    static EntityTickableModifier baseModifier(CruxSlotGroup slot){
        return modifier(Crux.key("base"), slot);
    }
    static EntityTickableModifier baseModifier(CruxSlotGroup slot, Key... path){
        return modifier(Crux.key("base"), slot, path);
    }*/

    static EntityTickableModifier modifier(Key key, EntityTickable tickable, CruxSlotGroup slot){
        return new SimpleEntityTickableModifier(key, tickable, slot);
    }
    static EntityTickableModifier modifier(Key key, EntityTickable tickable){
        return modifier(key, tickable, null);
    }
    /*static EntityTickableModifier baseModifier(){
        return modifier(Crux.key("base"), null);
    }
    static EntityTickableModifier baseModifier(CruxSlotGroup slot){
        return modifier(Crux.key("base"), slot);
    }
    static EntityTickableModifier baseModifier(CruxSlotGroup slot, Key... path){
        return modifier(Crux.key("base"), slot, path);
    }*/

    Map<?, ?> getData();
    @NotNull EntityTickable getTickable();
    @NotNull
    CruxSlotGroup getSlotGroup();
}
