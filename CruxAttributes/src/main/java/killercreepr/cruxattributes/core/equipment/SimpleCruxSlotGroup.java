package killercreepr.cruxattributes.core.equipment;

import killercreepr.cruxattributes.api.equipment.CruxSlot;
import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxattributes.core.registries.CruxAttributeRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

public class SimpleCruxSlotGroup implements CruxSlotGroup {
    protected final Key key;
    protected final Predicate<CruxSlot> filter;
    protected final Collection<CruxSlot> slots;

    public SimpleCruxSlotGroup(Key key, Predicate<CruxSlot> filter) {
        this.key = key;
        this.filter = filter;
        this.slots = null;
    }
    public SimpleCruxSlotGroup(Key key, CruxSlot slot) {
        this.key = key;
        this.filter = e -> e.compare(slot);
        this.slots = Set.of(slot);
    }
    public SimpleCruxSlotGroup(Key key, CruxSlot... slot) {
        this.key = key;
        Collection<CruxSlot> slots = Set.of(slot);
        this.filter = slots::contains;
        this.slots = slots;
    }
    public SimpleCruxSlotGroup(Key key, Collection<CruxSlot> slots) {
        this.key = key;
        this.filter = slots::contains;
        this.slots = slots;
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
}
