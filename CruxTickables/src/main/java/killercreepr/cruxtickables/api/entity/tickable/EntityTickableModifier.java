package killercreepr.cruxtickables.api.entity.tickable;

import killercreepr.crux.api.data.CruxKeyed;
import killercreepr.crux.core.Crux;
import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxtickables.api.equipment.SetBonus;
import killercreepr.cruxtickables.core.entity.tickable.SimpleEntityTickableModifier;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EntityTickableModifier extends CruxKeyed {
    static EntityTickableModifier modifier(Key key, EntityTickable tickable, CruxSlotGroup slot, SetBonus setBonus){
        return modifier(key, tickable, slot,  setBonus, null);
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

    static EntityTickableModifier empty(EntityTickable tickable){
        return modifier(Crux.key("empty"), tickable);
    }

    Object getData();
    @NotNull EntityTickable getTickable();
    @NotNull
    CruxSlotGroup getSlotGroup();
    @Nullable SetBonus getSetBonus();

    @Contract(pure = true)
    EntityTickableModifier withData(Object data);
    @Contract(pure = true)
    EntityTickableModifier withKey(Key key);
    @Contract(pure = true)
    EntityTickableModifier withTickable(EntityTickable tickable);
    @Contract(pure = true)
    EntityTickableModifier withSlotGroup(CruxSlotGroup slotGroup);
    @Contract(pure = true)
    EntityTickableModifier withSetBonus(SetBonus setBonus);

}
