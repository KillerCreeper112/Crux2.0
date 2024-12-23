package killercreepr.cruxstructures.api.event;

import killercreepr.cruxstructures.api.structure.Structure;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class StructureEvent extends Event {
    protected final @NotNull Structure structure;
    public StructureEvent(@NotNull Structure structure) {
        this.structure = structure;
    }
    public StructureEvent(@NotNull Structure structure, boolean isAsync) {
        super(isAsync);
        this.structure = structure;
    }

    public @NotNull Structure getStructure() {
        return structure;
    }
}
