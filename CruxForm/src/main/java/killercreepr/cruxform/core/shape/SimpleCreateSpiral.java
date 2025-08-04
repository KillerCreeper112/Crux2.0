package killercreepr.cruxform.core.shape;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxform.api.shape.CreateSpiral;
import killercreepr.cruxform.api.shape.cache.CreateCachedShape;
import killercreepr.cruxform.core.shape.cache.SimpleCachedAddRelative;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SimpleCreateSpiral implements CreateSpiral {
    protected final Holder<CruxLocation> center;
    protected final NumberProvider radius;
    protected final NumberProvider height;
    protected final NumberProvider spacing;
    protected final NumberProvider turns;
    protected final NumberProvider spiralGrowth; // ⬅ New: controls inward/outward rate
    protected final boolean invertX;
    protected final boolean invertZ;

    public SimpleCreateSpiral(
        Holder<CruxLocation> center,
        NumberProvider radius,
        NumberProvider height,
        NumberProvider spacing,
        NumberProvider turns,
        NumberProvider spiralGrowth,
        boolean invertX,
        boolean invertZ
    ) {
        this.center = center;
        this.radius = radius;
        this.height = height;
        this.spacing = spacing;
        this.turns = turns;
        this.spiralGrowth = spiralGrowth;
        this.invertX = invertX;
        this.invertZ = invertZ;
    }

    public List<Vector> generateVectors() {
        List<Vector> vectors = new ArrayList<>();

        double baseRadius = this.radius.value().doubleValue();
        double totalHeight = this.height.value().doubleValue();
        double spacing = this.spacing.value().doubleValue();
        double turnCount = this.turns.value().doubleValue();
        double growth = this.spiralGrowth.value().doubleValue(); // ⬅ outward if positive, inward if negative

        int invertX = this.invertX ? -1 : 1;
        int invertZ = this.invertZ ? -1 : 1;

        double maxAngle = 2 * Math.PI * turnCount;
        int pointCount = (int) (maxAngle / spacing);
        double heightPerStep = totalHeight / pointCount;

        for (int i = 0; i <= pointCount; i++) {
            double angle = i * spacing;
            double currentRadius = baseRadius + (growth * angle);
            if (currentRadius < 0) continue; // skip impossible radius

            double x = currentRadius * Math.cos(angle) * invertX;
            double z = currentRadius * Math.sin(angle) * invertZ;
            double y = heightPerStep * i;

            vectors.add(new Vector(x, y, z));
        }

        return vectors;
    }

    @Override
    public void generate(@NotNull Consumer<CruxPosition> consumer) {
        generateVectors().forEach(vec -> {
            CruxLocation location = center.value();
            vec.rotateAroundX(Math.toRadians(location.pitch()));
            vec.rotateAroundY(Math.toRadians(-location.yaw()));

            CruxLocation result = location.add(vec.getX(), vec.getY(), vec.getZ());
            consumer.accept(result);
        });
    }

    @Override
    public @NotNull CreateCachedShape generateCache(@Nullable Consumer<Vector> consumer) {
        return new SimpleCachedAddRelative(generateVectors(), center);
    }

    public static class Builder implements CreateSpiral.Builder {

        private Holder<CruxLocation> center;
        private NumberProvider radius;
        private NumberProvider height;
        private NumberProvider spacing;
        private NumberProvider turns;
        private NumberProvider spiralGrowth;
        private boolean invertX;
        private boolean invertZ;


        @Override
        public CreateSpiral.Builder center(Holder<CruxLocation> center) {
            this.center = center;
            return this;
        }

        @Override
        public CreateSpiral.Builder radius(NumberProvider radius) {
            this.radius = radius;
            return this;
        }

        @Override
        public CreateSpiral.Builder height(NumberProvider height) {
            this.height = height;
            return this;
        }

        @Override
        public CreateSpiral.Builder spacing(NumberProvider spacing) {
            this.spacing = spacing;
            return this;
        }

        @Override
        public CreateSpiral.Builder turns(NumberProvider turns) {
            this.turns = turns;
            return this;
        }

        @Override
        public CreateSpiral.Builder spiralGrowth(NumberProvider spiralGrowth) {
            this.spiralGrowth = spiralGrowth;
            return this;
        }

        @Override
        public CreateSpiral.Builder invertX(boolean invertX) {
            this.invertX = invertX;
            return this;
        }

        @Override
        public CreateSpiral.Builder invertZ(boolean invertZ) {
            this.invertZ = invertZ;
            return this;
        }

        @Override
        public CreateSpiral build() {
            if(radius == null) radius = NumberProvider.constant(1.5);
            if(height == null) height = NumberProvider.constant(3);
            if(spacing == null) spacing = NumberProvider.constant(.1);
            if(turns == null) turns = NumberProvider.constant(3);
            if(spiralGrowth == null) spiralGrowth = NumberProvider.zero();
            return new SimpleCreateSpiral(
                center, radius, height, spacing, turns, spiralGrowth,
                invertX, invertZ
            );
        }
    }
}


/*
public class SimpleCreateSpiral implements CreateShape {
    protected final Holder<CruxLocation> center;
    protected final NumberProvider radius;
    protected final NumberProvider spacing;
    protected final NumberProvider height;
    protected final NumberProvider turns;
    protected final boolean invertX;
    protected final boolean invertZ;

    public SimpleCreateSpiral(Holder<CruxLocation> center,
                              NumberProvider radius,
                              NumberProvider spacing,
                              NumberProvider height,
                              NumberProvider turns,
                              boolean invertX,
                              boolean invertZ) {
        this.center = center;
        this.radius = radius;
        this.spacing = spacing;
        this.height = height;
        this.turns = turns;
        this.invertX = invertX;
        this.invertZ = invertZ;
    }

    public List<Vector> generateVectors() {
        List<Vector> list = new ArrayList<>();

        double r = this.radius.value().doubleValue();
        double spacing = this.spacing.value().doubleValue();
        double h = this.height.value().doubleValue();
        double t = this.turns.value().doubleValue();

        int invertX = this.invertX ? -1 : 1;
        int invertZ = this.invertZ ? -1 : 1;

        double totalHeight = h;
        double totalAngle = 2 * Math.PI * t;
        double angleStep = spacing / r; // angular step for given spacing

        for (double angle = 0; angle <= totalAngle; angle += angleStep) {
            double y = (angle / totalAngle) * totalHeight;

            double x = r * Math.cos(angle) * invertX;
            double z = r * Math.sin(angle) * invertZ;

            list.add(new Vector(x, y, z));
        }

        return list;
    }

    @Override
    public void generate(@NotNull Consumer<CruxPosition> consumer) {
        generateVectors().forEach(vec -> {
            CruxLocation location = center.value();
            vec.rotateAroundX(Math.toRadians(location.pitch()));
            vec.rotateAroundY(Math.toRadians(location.yaw() * -1));

            CruxLocation result = location.add(vec.getX(), vec.getY(), vec.getZ());
            consumer.accept(result);
        });
    }

    @Override
    public @NotNull CreateCachedShape generateCache(@Nullable Consumer<Vector> consumer) {
        return new SimpleCachedAddRelative(generateVectors(), center);
    }
}
*/
