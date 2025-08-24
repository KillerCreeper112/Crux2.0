package killercreepr.cruxstructures.api.world.event;

import killercreepr.cruxstructures.api.structure.StoredStructure;
import killercreepr.cruxworlds.api.world.event.WorldEvent;
import org.jetbrains.annotations.NotNull;

public interface TargetStructureWorldEvent extends WorldEvent {
    @NotNull StoredStructure getTargetStructure();
}
