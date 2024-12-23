package killercreepr.cruxform.core.shape;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxform.api.shape.CreateLine;
import killercreepr.cruxform.api.shape.cache.CreateCachedShape;
import killercreepr.cruxform.core.shape.cache.SimpleCachedShiftToward;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SimpleCreateLine implements CreateLine {
    protected final Holder<CruxPosition> start;
    protected final Holder<CruxPosition> end;
    protected final NumberProvider spacing;

    public SimpleCreateLine(Holder<CruxPosition> start, Holder<CruxPosition> end, NumberProvider spacing) {
        this.start = start;
        this.end = end;
        this.spacing = spacing;
    }

    @Override
    public void generate(@NotNull Consumer<CruxPosition> consumer) {
        CruxLocation startLooking = CruxLocation.location(start.value());
        generateVectors().forEach(vec ->{
            //CruxPosition location = start.value();
            CruxLocation result = startLooking.addRelative(vec.getX(), vec.getY(), vec.getZ());
            consumer.accept(result);
        });
    }

    public List<Vector> generateVectors(){
        List<Vector> list = new ArrayList<>();
        CruxPosition start = this.start.value();
        CruxPosition end = this.end.value();

        double distance = Math.sqrt(start.distanceSquared(end));
        double spacing = this.spacing.value().doubleValue();

        for(double currentDistance = 0D; currentDistance <= distance; currentDistance += spacing){
            Vector vec = new Vector(currentDistance, 0, 0);
            list.add(vec);
            if(currentDistance > distance) break;
        }
        return list;
    }

    @Override
    public @NotNull CreateCachedShape generateCache(@Nullable Consumer<Vector> consumer) {
        return new SimpleCachedShiftToward(generateVectors(), start, end);
    }
    public static class Builder implements CreateLine.Builder {
        private Holder<CruxPosition> start;
        private Holder<CruxPosition> end;
        private NumberProvider spacing;

        @Override
        public CreateLine.Builder start(Holder<CruxPosition> start) {
            this.start = start;
            return this;
        }

        @Override
        public CreateLine.Builder end(Holder<CruxPosition> end) {
            this.end = end;
            return this;
        }

        @Override
        public CreateLine.Builder spacing(NumberProvider spacing) {
            this.spacing = spacing;
            return this;
        }

        @Override
        public CreateLine build() {
            return new SimpleCreateLine(start, end, spacing);
        }
    }
}
