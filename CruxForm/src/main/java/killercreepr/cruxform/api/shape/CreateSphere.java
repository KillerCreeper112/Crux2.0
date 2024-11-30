package killercreepr.cruxform.api.shape;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxform.core.shape.SimpleCreateSphere;

public interface CreateSphere extends CreateShape{
    static Builder builder(){
        return new SimpleCreateSphere.Builder();
    }

    interface Builder{
        Builder center(Holder<CruxLocation> center);
        default Builder center(CruxLocation center){
            return center(Holder.direct(center));
        }
        Builder radius(NumberProvider radius);
        Builder spacing(NumberProvider spacing);
        default Builder radius(double radius){
            return radius(NumberProvider.constant(radius));
        }
        default Builder spacing(double spacing){
            return spacing(NumberProvider.constant(spacing));
        }

        Builder invertX(boolean invertX);
        Builder invertY(boolean invertY);
        Builder invertZ(boolean invertZ);

        Builder type(Type type);
        CreateSphere build();
    }

    enum Type{
        WHOLE,
        HOLLOW
    }
}
