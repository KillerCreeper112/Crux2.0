package killercreepr.cruxstructures.core.structure.generation.result;

import killercreepr.cruxstructures.api.event.StructurePlaceEvent;
import killercreepr.cruxstructures.api.structure.generation.result.GenerateResult;
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

