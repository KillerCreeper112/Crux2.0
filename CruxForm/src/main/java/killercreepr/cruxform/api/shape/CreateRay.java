package killercreepr.cruxform.api.shape;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxform.core.shape.SimpleCreateRay;

public interface CreateRay extends CreateShape{
    static Builder builder(){
        return new SimpleCreateRay.Builder();
    }
    interface Builder {
        Builder start(Holder<CruxLocation> start);
        default Builder start(CruxLocation start){
            return start(Holder.direct(start));
        }
        Builder length(NumberProvider length);
        default Builder length(double length){
            return length(NumberProvider.constant(length));
        }
        Builder spacing(NumberProvider spacing);
        default Builder spacing(double spacing){
            return spacing(NumberProvider.constant(spacing));
        }
        CreateRay build();
    }
}
