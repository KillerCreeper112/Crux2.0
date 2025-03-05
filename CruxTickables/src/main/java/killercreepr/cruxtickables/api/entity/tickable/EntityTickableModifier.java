package killercreepr.cruxtickables.api.entity.tickable;

import killercreepr.crux.api.data.CruxKeyed;
import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxtickables.api.equipment.SetBonus;
import killercreepr.cruxtickables.core.entity.tickable.SimpleEntityTickableModifier;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EntityTickableModifier extends CruxKeyed {
    static EntityTickableModifier modifier(Key key, EntityTickable tickable, CruxSlotGroup slot, Object data){
        return new SimpleEntityTickableModifier(key, tickable, slot, null, data);
    }
    static EntityTickableModifier modifier(Key key, EntityTickable tickable, CruxSlotGroup slot, SetBonus setBonus, Object data){
        return new SimpleEntityTickableModifier(key, tickable, slot, setBonus, data);
    }
    static EntityTickableModifier modifier(Key key, EntityTickable tickable, CruxSlotGroup slot){
        return modifier(key, tickable, slot, null);
    }
    static EntityTickableModifier modifier(Key key, EntityTickable tickable){
        return modifier(key, tickable, null);
    }

    Object getData();
    @NotNull EntityTickable getTickable();
    @NotNull
    CruxSlotGroup getSlotGroup();
    @Nullable SetBonus getSetBonus();
}
