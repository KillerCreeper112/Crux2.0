package killercreepr.cruxform.api.scheduler.context;

import killercreepr.crux.api.math.CruxLocation;
import org.jetbrains.annotations.NotNull;

public interface ShapeTickContext {
    int getTick();
    @NotNull
    CruxLocation getLocation();
    int getSize();

    default float getProgress(){
        return (float) getTick() / (float) getSize();
    }
}
