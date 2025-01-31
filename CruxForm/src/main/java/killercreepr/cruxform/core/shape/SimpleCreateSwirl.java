package killercreepr.cruxform.core.shape;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxform.api.shape.CreateSwirl;
import killercreepr.cruxform.api.shape.cache.CreateCachedShape;
import killercreepr.cruxform.core.shape.cache.SimpleCachedAddRelative;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

//todo make pitch and yaw work properly, the spiral should point in the direction
public class SimpleCreateSwirl implements CreateSwirl {
    protected final Holder<CruxLocation> center;
    protected final NumberProvider radius;
    protected final NumberProvider height;
    protected final NumberProvider spacing;
    protected final NumberProvider turns;
    protected final NumberProvider radiusFactor;

    public SimpleCreateSwirl(Holder<CruxLocation> center, NumberProvider radius, NumberProvider height, NumberProvider spacing, NumberProvider turns, NumberProvider radiusFactor) {
        this.center = center;
        this.radius = radius;
        this.height = height;
        this.spacing = spacing;
        this.turns = turns;
        this.radiusFactor = radiusFactor;
    }


    @Override
    public void generate(@NotNull Consumer<CruxPosition> consumer) {
        generateVectors().forEach(vec ->{
            CruxLocation location = center.value();
            CruxLocation result = location.addRelative(vec.getX(), vec.getY(), vec.getZ());
            consumer.accept(result);
        });
    }

    public List<Vector> generateVectors(){
        List<Vector> vectors = new ArrayList<>();
        double height = this.height.value().doubleValue();
        double turns = this.turns.value().doubleValue();
        double radius = this.turns.value().doubleValue();
        double spacing = this.spacing.value().doubleValue();
        double radiusFactor = this.radiusFactor.value().doubleValue();  // User-defined radius factor

        int points = (int) (height / spacing); // Calculate number of points based on spacing
        double heightStep = height / points; // Step per point in height
        double angleStep = (2 * Math.PI * turns) / points; // Spread points evenly across turns

        for (int i = 0; i < points; i++) {
            double angle = i * angleStep;

            // Prevent radius from decreasing if radiusFactor is 0
            double currentRadius;
            if (radiusFactor == 0) {
                currentRadius = radius; // No decrease, radius stays constant
            } else {
                // Adjust how the radius decreases based on the radiusFactor
                double radiusDecrement = Math.pow(1 - (double) i / points, radiusFactor);  // Modify decrease rate using radiusFactor
                currentRadius = radius * radiusDecrement; // Apply factor to radius decrease
            }

            double x = currentRadius * Math.cos(angle);
            double z = currentRadius * Math.sin(angle);
            double y = i * heightStep;

            // Create the initial vector
            Vector point = new Vector(x, y, z);

            // Rotate the vector based on location yaw and pitch
            CruxLocation loc = center.value();
            CruxMath.rotateVector(point, loc.yaw(), loc.pitch());

            // Add to the list
            vectors.add(point);
        }

        return vectors;

        /*List<Vector> vectors = new ArrayList<>();
        double height = this.height.value().doubleValue();
        double turns = this.turns.value().doubleValue();
        double radius = this.turns.value().doubleValue();
        double spacing = this.spacing.value().doubleValue();

        int points = (int) (height / spacing); // Calculate number of points based on spacing
        double heightStep = height / points; // Step per point in height
        double angleStep = (2 * Math.PI * turns) / points; // Spread points evenly across turns


        for (int i = 0; i < points; i++) {
            double angle = i * angleStep;
            double currentRadius = radius * (1 - (double) i / points); // Gradually decrease radius (optional)
            double x = currentRadius * Math.cos(angle);
            double z = currentRadius * Math.sin(angle);
            double y = i * heightStep;

            // Create the initial vector
            Vector point = new Vector(x, y, z);

            // Rotate the vector based on location yaw and pitch
            CruxLocation loc = center.value();
            CruxMath.rotateVector(point, loc.yaw(), loc.pitch());

            // Add to the list
            vectors.add(point);
        }

        return vectors;*/
    }

    @Override
    public @NotNull CreateCachedShape generateCache(@Nullable Consumer<Vector> consumer) {
        return new SimpleCachedAddRelative(generateVectors(), center);
    }
    public static class Builder implements CreateSwirl.Builder {

        private Holder<CruxLocation> center;
        private NumberProvider radius;
        private NumberProvider height;
        private NumberProvider spacing;
        private NumberProvider turns;
        private NumberProvider radiusFactor;

        @Override
        public CreateSwirl.Builder radiusFactor(NumberProvider radiusFactor) {
            this.radiusFactor = radiusFactor;
            return this;
        }

        @Override
        public CreateSwirl.Builder center(Holder<CruxLocation> center) {
            this.center = center;
            return this;
        }

        @Override
        public CreateSwirl.Builder radius(NumberProvider radius) {
            this.radius = radius;
            return this;
        }

        @Override
        public CreateSwirl.Builder height(NumberProvider height) {
            this.height = height;
            return this;
        }

        @Override
        public CreateSwirl.Builder spacing(NumberProvider spacing) {
            this.spacing = spacing;
            return this;
        }

        @Override
        public CreateSwirl.Builder turns(NumberProvider turns) {
            this.turns = turns;
            return this;
        }

        @Override
        public CreateSwirl build() {
            if(radius == null) radius = NumberProvider.constant(1.5);
            if(height == null) height = NumberProvider.constant(3);
            if(spacing == null) spacing = NumberProvider.constant(.1);
            if(turns == null) turns = NumberProvider.constant(3);
            if(radiusFactor == null) radiusFactor = NumberProvider.constant(1);
            return new SimpleCreateSwirl(
                center, radius, height, spacing, turns, radiusFactor
            );
        }
    }
}
