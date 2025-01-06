package killercreepr.cruxform.api.shape;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxform.core.shape.SimpleCreateRay;
import killercreepr.cruxform.core.shape.SimpleCreateSwirl;

public interface CreateSwirl extends CreateShape{
    static Builder builder(){
        return new SimpleCreateSwirl.Builder();
    }
    interface Builder {
        Builder center(Holder<CruxLocation> center);
        default Builder center(CruxLocation center){
            return center(Holder.direct(center));
        }
        Builder radius(NumberProvider radius);
        default Builder radius(double radius){
            return radius(NumberProvider.constant(radius));
        }
        Builder height(NumberProvider height);
        default Builder height(double height){
            return height(NumberProvider.constant(height));
        }
        Builder spacing(NumberProvider spacing);
        default Builder spacing(double spacing){
            return spacing(NumberProvider.constant(spacing));
        }
        Builder turns(NumberProvider turns);
        default Builder turns(double turns){
            return turns(NumberProvider.constant(turns));
        }
        CreateSwirl build();
    }
}
