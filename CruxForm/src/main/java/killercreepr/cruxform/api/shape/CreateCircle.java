package killercreepr.cruxform.api.shape;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxform.core.shape.SimpleCreateCircle;

public interface CreateCircle extends CreateShape{
    static Builder builder(){
        return new SimpleCreateCircle.Builder();
    }

    interface Builder{
        Builder center(Holder<CruxLocation> center);
        default Builder center(CruxLocation center){
            return center(Holder.direct(center));
        }
        Builder radius(NumberProvider radius);
        Builder amountMultiplier(NumberProvider amountMultiplier);
        default Builder radius(double radius){
            return radius(NumberProvider.constant(radius));
        }
        default Builder amountMultiplier(double amountMultiplier){
            return amountMultiplier(NumberProvider.constant(amountMultiplier));
        }

        Builder spacing(NumberProvider spacing);
        default Builder spacing(double spacing){
            return spacing(NumberProvider.constant(spacing));
        }

        Builder invertX(boolean invertX);
        Builder invertZ(boolean invertZ);
        Builder type(Type type);
        CreateCircle build();
    }

    enum Type{
        WHOLE,
        HOLLOW,
        HARD_WHOLE,
        HARD_HOLLOW
    }
}
