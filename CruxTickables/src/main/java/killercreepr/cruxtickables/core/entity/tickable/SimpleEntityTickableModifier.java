package killercreepr.cruxtickables.core.entity.tickable;

import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickable;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickableModifier;
import org.jetbrains.annotations.NotNull;

public class SimpleEntityTickableModifier implements EntityTickableModifier {
    protected final EntityTickable tickable;
    protected final CruxSlotGroup slot;

    public SimpleEntityTickableModifier(EntityTickable tickable, CruxSlotGroup slot) {
        this.tickable = tickable;
        this.slot = slot;
    }

    @Override
    public @NotNull EntityTickable getTickable() {
        return tickable;
    }

    @Override
    public @NotNull CruxSlotGroup getSlotGroup() {
        return slot == null ? CruxSlotGroup.ANY : slot;
    }
}
