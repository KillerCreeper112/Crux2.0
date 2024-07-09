package killercreepr.cruxstructures.event;

import killercreepr.cruxstructures.structure.Structure;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class StructureEvent extends Event {
    protected final @NotNull Structure structure;
    public StructureEvent(@NotNull Structure structure) {
        this.structure = structure;
    }

    public @NotNull Structure getStructure() {
        return structure;
    }
}
