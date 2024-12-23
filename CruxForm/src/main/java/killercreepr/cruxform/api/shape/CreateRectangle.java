package killercreepr.cruxform.api.shape;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.util.CruxedBoundingBox;
import killercreepr.cruxform.core.shape.SimpleCreateRectangle;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

public interface CreateRectangle extends CreateShape{
    static Builder builder(){
        return new SimpleCreateRectangle.Builder();
    }
    interface Builder {
        Builder pos1(Holder<CruxPosition> pos1);
        Builder pos2(Holder<CruxPosition> pos2);
        default Builder pos1(CruxPosition pos1){
            return pos1(Holder.direct(pos1));
        }
        default Builder pos2(CruxPosition pos2){
            return pos2(Holder.direct(pos2));
        }
        Builder type(Type type);
        Builder spacing(NumberProvider spacing);
        default Builder spacing(double spacing){
            return spacing(NumberProvider.constant(spacing));
        }
        Builder inverted(boolean inverted);
        default Builder boundingBox(@NotNull BoundingBox box){
            return pos1(CruxPosition.location(box.getMinX(), box.getMinY(), box.getMinZ()))
                .pos2(CruxPosition.location(box.getMaxX(), box.getMaxY(), box.getMaxZ()));
        }
        default Builder boundingBox(CruxedBoundingBox box){
            return boundingBox(box.box());
        }
        default Builder centerRange(@NotNull CruxPosition center, double size){
            size = size / 2D;
            CruxPosition pos1 = center.add(-size, -size, -size);
            CruxPosition pos2 = center.add(size, size, size);
            return pos1(pos1).pos2(pos2);
        }
        CreateRectangle build();
    }

    enum Type{
        WHOLE,
        HOLLOW,
        WIRE,
        WALLS
    }
}
