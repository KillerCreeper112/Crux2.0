package killercreepr.cruxtickables.core.entity.tickable;

import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickableModifier;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class SimpleEntityTickableModifier implements EntityTickableModifier {
    protected final Key key;
    protected final CruxSlotGroup slot;
    protected final Key[] path;

    public SimpleEntityTickableModifier(Key key, CruxSlotGroup slot, Key[] path) {
        this.key = key;
        this.slot = slot;
        this.path = path;
    }

    @Contract(pure = true)
    public EntityTickableModifier withPath(Key[] path){
        return new SimpleEntityTickableModifier(key, slot, path);
    }

    @Override
    public @NotNull CruxSlotGroup getSlotGroup() {
        return slot == null ? CruxSlotGroup.ANY : slot;
    }

    @Override
    public Key[] getPath() {
        return path;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
