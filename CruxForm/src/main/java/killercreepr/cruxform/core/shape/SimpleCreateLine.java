package killercreepr.cruxform.core.shape;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxLocation;
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
    protected final Holder<CruxLocation> start;
    protected final Holder<CruxLocation> end;
    protected final NumberProvider spacing;

    public SimpleCreateLine(Holder<CruxLocation> start, Holder<CruxLocation> end, NumberProvider spacing) {
        this.start = start;
        this.end = end;
        this.spacing = spacing;
    }

    @Override
    public void generate(@NotNull Consumer<CruxLocation> consumer) {
        generateVectors().forEach(vec ->{
            CruxLocation location = start.value();
            CruxLocation result = location.addRelative(vec.getX(), vec.getY(), vec.getZ());
            consumer.accept(result);
        });
    }

    public List<Vector> generateVectors(){
        List<Vector> list = new ArrayList<>();
        CruxLocation start = this.start.value();
        CruxLocation end = this.end.value();

        double distance = start.distanceSquared(end);
        double spacing = this.spacing.value().doubleValue();
        double spacingSquared = spacing * spacing;

        double realDistance = 0D;
        for(double currentDistance = 0D; currentDistance <= distance; currentDistance += spacingSquared){
            Vector vec = new Vector(realDistance, 0, 0);
            list.add(vec);
            if(currentDistance > distance) break;
            realDistance += spacing;
        }
        return list;
    }

    @Override
    public @NotNull CreateCachedShape generateCache(@Nullable Consumer<Vector> consumer) {
        return new SimpleCachedShiftToward(generateVectors(), start, end);
    }
    public static class Builder implements CreateLine.Builder {
        private Holder<CruxLocation> start;
        private Holder<CruxLocation> end;
        private NumberProvider spacing;

        @Override
        public CreateLine.Builder start(Holder<CruxLocation> start) {
            this.start = start;
            return this;
        }

        @Override
        public CreateLine.Builder end(Holder<CruxLocation> end) {
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
