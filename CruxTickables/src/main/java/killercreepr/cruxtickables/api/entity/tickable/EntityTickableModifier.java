package killercreepr.cruxtickables.api.entity.tickable;

import killercreepr.crux.api.data.CruxKeyed;
import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxtickables.core.entity.tickable.SimpleEntityTickableModifier;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public interface EntityTickableModifier extends CruxKeyed {
    static EntityTickableModifier modifier(EntityTickable tickable, CruxSlotGroup slot){
        return new SimpleEntityTickableModifier(tickable, slot);
    }
    static EntityTickableModifier modifier(EntityTickable tickable){
        return modifier(tickable, null);
    }

    @NotNull EntityTickable getTickable();
    @NotNull
    CruxSlotGroup getSlotGroup();
    Key[] getPath();
}
