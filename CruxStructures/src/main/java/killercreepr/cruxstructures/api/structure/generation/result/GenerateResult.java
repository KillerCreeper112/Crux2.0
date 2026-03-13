package killercreepr.cruxstructures.api.structure.generation.result;

import killercreepr.cruxstructures.api.event.StructurePlaceEvent;
import killercreepr.cruxstructures.core.structure.generation.result.SimpleGenerateResult;
import org.jetbrains.annotations.Nullable;

public interface GenerateResult {
    GenerateResult EMPTY = new SimpleGenerateResult(null);
    static GenerateResult empty(){
        return EMPTY;
    }

    static GenerateResult result(@Nullable StructurePlaceEvent event){
        return new SimpleGenerateResult(event);
    }

    @Nullable StructurePlaceEvent getPlaceEvent();

    default boolean wasPlaced(){
        var event = getPlaceEvent();
        return event != null && !event.isCancelled();
    }
}
