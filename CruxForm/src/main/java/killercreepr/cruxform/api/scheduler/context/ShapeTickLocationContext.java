package killercreepr.cruxform.api.scheduler.context;

import killercreepr.crux.api.math.CruxLocation;
import org.jetbrains.annotations.NotNull;

public interface ShapeTickLocationContext extends ShapeTickContext {
    int getIteration();
    @NotNull
    CruxLocation getLocation();
    default float getProgress(){
        return (float) getIteration() / (float) getLocationAmount();
    }
}
