package killercreepr.cruxtickables.core.entity.tickable;

import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickable;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickableModifier;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class SimpleEntityTickableModifier implements EntityTickableModifier {
    protected final Key key;
    protected final EntityTickable tickable;
    protected final CruxSlotGroup slot;
    protected final Object data;

    public SimpleEntityTickableModifier(Key key, EntityTickable tickable, CruxSlotGroup slot, Object data) {
        this.key = key;
        this.tickable = tickable;
        this.slot = slot;
        this.data = data;
    }


    @Override
    public Object getData() {
        return data;
    }

    @Override
    public @NotNull EntityTickable getTickable() {
        return tickable;
    }

    @Override
    public @NotNull CruxSlotGroup getSlotGroup() {
        return slot == null ? CruxSlotGroup.ANY : slot;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
