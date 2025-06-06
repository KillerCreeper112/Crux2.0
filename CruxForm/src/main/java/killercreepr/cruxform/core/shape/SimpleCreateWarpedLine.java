package killercreepr.cruxform.core.shape;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxform.api.shape.CreateWarpedLine;
import killercreepr.cruxform.api.shape.cache.CreateCachedShape;
import killercreepr.cruxform.core.shape.cache.SimpleCachedAddCenter;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SimpleCreateWarpedLine extends SimpleCreateLine implements CreateWarpedLine {
    protected final NumberProvider warpStrength;
    protected final NumberProvider tickOffset;
    public SimpleCreateWarpedLine(Holder<CruxPosition> start, Holder<CruxPosition> end, NumberProvider spacing, NumberProvider warpStrength, NumberProvider tickOffset) {
        super(start, end, spacing);
        this.warpStrength = warpStrength;
        this.tickOffset = tickOffset;
    }

    @Override
    public void generate(@NotNull Consumer<CruxPosition> consumer) {
        CruxLocation origin = CruxLocation.location(start.value());
        generateVectors().forEach(vec -> consumer.accept(origin.add(vec.getX(), vec.getY(), vec.getZ())));
    }

    public List<Vector> generateVectors(){
        List<Vector> list = new ArrayList<>();
        Vector from = start.value().toVector();
        Vector to = end.value().toVector();

        Vector direction = to.clone().subtract(from);
        double totalLength = direction.length();
        direction.normalize();

        double spacingVal = spacing.value().doubleValue();
        double strengthVal = warpStrength.value().doubleValue();
        double time = tickOffset.value().doubleValue();

        Vector up = new Vector(0, 1, 0);
        if (Math.abs(direction.dot(up)) > 0.9) up = new Vector(1, 0, 0);
        Vector perp = direction.clone().crossProduct(up).normalize();

        for (double dist = 0; dist <= totalLength; dist += spacingVal) {
            double t = dist / totalLength;

            double wave = Math.sin(t * Math.PI * 4 + time);
            double yWarp = Math.cos(t * Math.PI * 3 + time * 0.5);

            Vector base = direction.clone().multiply(dist);
            Vector offset = perp.clone().multiply(wave * strengthVal);
            offset.setY(yWarp * strengthVal * 0.5);

            list.add(base.add(offset));
        }

        return list;
    }

    @Override
    public @NotNull CreateCachedShape generateCache(@Nullable Consumer<Vector> consumer) {
        return new SimpleCachedAddCenter(generateVectors(), () -> CruxLocation.location(start.value()));
    }
    public static class Builder implements CreateWarpedLine.Builder {
        private Holder<CruxPosition> start;
        private Holder<CruxPosition> end;
        private NumberProvider spacing;
        private NumberProvider warpStrength;
        private NumberProvider tickOffset;

        @Override
        public CreateWarpedLine.Builder start(Holder<CruxPosition> start) {
            this.start = start;
            return this;
        }

        @Override
        public CreateWarpedLine.Builder end(Holder<CruxPosition> end) {
            this.end = end;
            return this;
        }

        @Override
        public CreateWarpedLine.Builder spacing(NumberProvider spacing) {
            this.spacing = spacing;
            return this;
        }

        @Override
        public CreateWarpedLine.Builder warpStrength(NumberProvider warpStrength) {
            this.warpStrength = warpStrength;
            return this;
        }

        @Override
        public CreateWarpedLine.Builder tickOffset(NumberProvider tickOffset) {
            this.tickOffset = tickOffset;
            return this;
        }

        @Override
        public CreateWarpedLine build() {
            return new SimpleCreateWarpedLine(start, end, spacing, warpStrength, tickOffset);
        }
    }
}
