package killercreepr.cruxtickables.core.entity.tickable;

import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickable;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickableModifier;
import killercreepr.cruxtickables.api.equipment.SetBonus;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SimpleEntityTickableModifier implements EntityTickableModifier {
    protected final Key key;
    protected final EntityTickable tickable;
    protected final CruxSlotGroup slot;
    protected final SetBonus setBonus;
    protected final Object data;

    public SimpleEntityTickableModifier(Key key, EntityTickable tickable, CruxSlotGroup slot, SetBonus setBonus, Object data) {
        this.key = key;
        this.tickable = tickable;
        this.slot = slot;
        this.setBonus = setBonus;
        this.data = data;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SimpleEntityTickableModifier that)) return false;
        return Objects.equals(key, that.key) && tickable.compare(that.tickable) &&
            ((slot == null && that.slot == null) || (slot != null && slot.compare(that.slot)))
            && Objects.equals(setBonus, that.setBonus) && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, tickable, slot, setBonus, data);
    }

    @Override
    public String toString() {
        return "SimpleEntityTickableModifier{" +
            "key=" + key +
            ", tickable=" + tickable +
            ", slot=" + slot +
            ", setBonus=" + setBonus +
            ", data=" + data +
            '}';
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
    public @Nullable SetBonus getSetBonus() {
        return setBonus;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
