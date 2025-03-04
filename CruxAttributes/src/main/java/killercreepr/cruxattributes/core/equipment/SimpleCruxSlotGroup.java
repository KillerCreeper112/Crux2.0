package killercreepr.cruxattributes.core.equipment;

import killercreepr.cruxattributes.api.equipment.CruxSlot;
import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxattributes.core.registries.CruxAttributeRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class SimpleCruxSlotGroup implements CruxSlotGroup {
    protected final Key key;
    protected final Predicate<CruxSlot> filter;
    protected final List<CruxSlot> slots;
    protected final String whenInSlot;

    public SimpleCruxSlotGroup(Key key, Predicate<CruxSlot> filter, String whenInSlot) {
        this.key = key;
        this.filter = filter;
        this.whenInSlot = whenInSlot;
        this.slots = null;
    }
    public SimpleCruxSlotGroup(Key key, CruxSlot slot, String whenInSlot) {
        this.key = key;
        this.filter = e -> e.compare(slot);
        this.slots = List.of(slot);
        this.whenInSlot = whenInSlot;
    }
    public SimpleCruxSlotGroup(Key key, String whenInSlot, CruxSlot... slot) {
        this.key = key;
        this.whenInSlot = whenInSlot;
        List<CruxSlot> slots = List.of(slot);
        this.filter = slots::contains;
        this.slots = slots;
    }
    public SimpleCruxSlotGroup(Key key, List<CruxSlot> slots, String whenInSlot) {
        this.key = key;
        this.filter = slots::contains;
        this.slots = slots;
        this.whenInSlot = whenInSlot;
    }

    @Override
    public String getWhenInSlot() {
        return whenInSlot;
    }

    @Override
    public boolean test(@NotNull CruxSlot slot) {
        return filter.test(slot);
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public @NotNull Iterator<CruxSlot> iterator() {
        if(slots == null) return CruxAttributeRegistries.SLOT.iterator();
        return slots.iterator();
    }

    @Override
    public @NotNull String translateKey() {
        return "cruxslotgroup/" + key.asString();
    }

    @Override
    public int compareTo(@NotNull CruxSlotGroup o) {
        return key().compareTo(o.key());
    }
}
