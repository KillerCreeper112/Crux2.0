package killercreepr.cruxform.api.shape;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxform.core.shape.SimpleCreateLine;

public interface CreateLine extends CreateShape{
    static Builder builder(){
        return new SimpleCreateLine.Builder();
    }
    interface Builder {
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
        CreateLine build();
    }
}
