package killercreepr.cruxform.api.shape;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxform.core.shape.SimpleCreateWarpedLine;

public interface CreateWarpedLine extends CreateLine{
    static Builder builder(){
        return new SimpleCreateWarpedLine.Builder();
    }
    interface Builder extends CreateLine.Builder {
        Builder start(Holder<CruxPosition> start);
        default Builder start(CruxPosition start){
            return start(Holder.direct(start));
        }
        Builder end(Holder<CruxPosition> end);
        default Builder end(CruxPosition end){
            return end(Holder.direct(end));
        }
        Builder spacing(NumberProvider spacing);
        default Builder spacing(double spacing){
            return spacing(NumberProvider.constant(spacing));
        }
        Builder warpStrength(NumberProvider warpStrength);
        default Builder warpStrength(double warpStrength){
            return warpStrength(NumberProvider.constant(warpStrength));
        }
        Builder tickOffset(NumberProvider tickOffset);
        default Builder tickOffset(double tickOffset){
            return tickOffset(NumberProvider.constant(tickOffset));
        }
        CreateWarpedLine build();
    }
}
