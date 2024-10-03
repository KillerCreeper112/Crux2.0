package killercreepr.cruxstructures.structure.result;

import killercreepr.cruxstructures.event.StructurePlaceEvent;
import org.jetbrains.annotations.Nullable;

public class SimpleGenerateResult implements GenerateResult {
    protected final @Nullable StructurePlaceEvent placeEvent;
    public SimpleGenerateResult(@Nullable StructurePlaceEvent placeEvent) {
        this.placeEvent = placeEvent;
    }

    @Override
    public @Nullable StructurePlaceEvent getPlaceEvent() {
        return placeEvent;
    }
}

