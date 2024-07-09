package killercreepr.cruxstructures.structure;

import killercreepr.cruxstructures.event.StructurePlaceEvent;
import org.jetbrains.annotations.Nullable;

public class GenerateResult {
    protected final @Nullable StructurePlaceEvent placeEvent;
    public GenerateResult(@Nullable StructurePlaceEvent placeEvent) {
        this.placeEvent = placeEvent;
    }

    public @Nullable StructurePlaceEvent getPlaceEvent() {
        return placeEvent;
    }
}
