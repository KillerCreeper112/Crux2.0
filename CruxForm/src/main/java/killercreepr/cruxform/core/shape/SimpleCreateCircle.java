package killercreepr.cruxform.core.shape;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxform.api.shape.CreateCircle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SimpleCreateCircle implements CreateCircle {
    protected final Holder<CruxLocation> center;
    protected final NumberProvider radius;
    protected final NumberProvider amountMultiplier;
    protected final NumberProvider time;
    protected final boolean invertX;
    protected final boolean invertZ;

    public SimpleCreateCircle(Holder<CruxLocation> center, NumberProvider radius, NumberProvider amountMultiplier, NumberProvider time, boolean invertX, boolean invertZ) {
        this.center = center;
        this.radius = radius;
        this.amountMultiplier = amountMultiplier;
        this.time = time;
        this.invertX = invertX;
        this.invertZ = invertZ;
    }

    @Override
    public void generate(@NotNull Consumer<CruxLocation> consumer) {
        double radius = this.radius.value().doubleValue();
        int parAmount = (int) (8f * radius);
        parAmount *= (int) amountMultiplier.value().doubleValue();

        int particleAmount = parAmount / 2;
        int iterations = parAmount;

        double t = 0D;
        for(; iterations > 0; iterations--){
            t = t + Math.PI / particleAmount;
            double x = radius * Math.cos(t);
            double z = radius * Math.sin(t);
            Vector vec = new Vector(x, 0, z);
            CruxLocation location = center.value();
            vec.rotateAroundX(location.pitch());
            vec.rotateAroundY(location.yaw());

            CruxLocation result = location.add(vec.getX(), vec.getY(), vec.getZ());
            consumer.accept(result);
        }
    }

    public static class Builder implements CreateCircle.Builder{
        private Holder<CruxLocation> center;
        private NumberProvider radius;
        private NumberProvider amountMultiplier;
        private NumberProvider time;
        private boolean invertX;
        private boolean invertZ;

        @Override
        public Builder center(Holder<CruxLocation> center) {
            this.center = center;
            return this; // Enables fluent interface
        }

        @Override
        public Builder radius(NumberProvider radius) {
            this.radius = radius;
            return this;
        }

        @Override
        public Builder amountMultiplier(NumberProvider amountMultiplier) {
            this.amountMultiplier = amountMultiplier;
            return this;
        }

        @Override
        public Builder time(NumberProvider time) {
            this.time = time;
            return this;
        }

        @Override
        public Builder invertX(boolean invertX) {
            this.invertX = invertX;
            return this;
        }

        @Override
        public Builder invertZ(boolean invertZ) {
            this.invertZ = invertZ;
            return this;
        }

        @Override
        public CreateCircle build() {
            return new SimpleCreateCircle(center, radius, amountMultiplier, time, invertX, invertZ);
        }
    }
}
