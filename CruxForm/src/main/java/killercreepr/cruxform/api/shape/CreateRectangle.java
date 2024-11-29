package killercreepr.cruxform.api.shape;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxform.core.shape.SimpleCreateRectangle;

public interface CreateRectangle extends CreateShape{
    static Builder builder(){
        return new SimpleCreateRectangle.Builder();
    }
    interface Builder {
        Builder pos1(Holder<CruxLocation> pos1);
        Builder pos2(Holder<CruxLocation> pos2);
        default Builder pos1(CruxLocation pos1){
            return pos1(Holder.direct(pos1));
        }
        default Builder pos2(CruxLocation pos2){
            return pos2(Holder.direct(pos2));
        }
        Builder type(Type type);
        Builder spacing(NumberProvider spacing);
        default Builder spacing(double spacing){
            return spacing(NumberProvider.constant(spacing));
        }
        Builder time(NumberProvider time);
        default Builder time(double time){
            return time(NumberProvider.constant(time));
        }
        Builder inverted(boolean inverted);
        CreateRectangle build();
    }

    enum Type{
        WHOLE,
        HOLLOW,
        WIRE,
        WALLS
    }
}
