package killercreepr.cruxform.api.scheduler.context;

import killercreepr.crux.api.math.CruxPosition;
import org.jetbrains.annotations.NotNull;

public interface ShapeTickLocationContext extends ShapeTickContext {
    int getIteration();
    @NotNull
    CruxPosition getLocation();
    default float getIterationProgress(){
        return (float) getIteration() / (float) getLocationAmount();
    }
}
