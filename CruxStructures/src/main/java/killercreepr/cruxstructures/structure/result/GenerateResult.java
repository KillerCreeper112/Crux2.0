package killercreepr.cruxstructures.structure.result;

import killercreepr.cruxstructures.event.StructurePlaceEvent;
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
}
