package killercreepr.cruxform.core.shape;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxform.api.shape.CreateCircle;
import killercreepr.cruxform.api.shape.cache.CreateCachedShape;
import killercreepr.cruxform.core.shape.cache.SimpleCachedCircle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SimpleCreateCircle implements CreateCircle {
    protected final Holder<CruxLocation> center;
    protected final NumberProvider radius;
    protected final NumberProvider amountMultiplier;
    protected final CreateCircle.Type type;
    protected final boolean invertX;
    protected final boolean invertZ;

    public SimpleCreateCircle(Holder<CruxLocation> center, NumberProvider radius, NumberProvider amountMultiplier, Type type, boolean invertX, boolean invertZ) {
        this.center = center;
        this.radius = radius;
        this.amountMultiplier = amountMultiplier;
        this.type = type;
        this.invertX = invertX;
        this.invertZ = invertZ;
    }

    private void generateFilledCircle(@NotNull Consumer<CruxLocation> consumer, double radius, int particleAmount) {
        for (double r = 0; r <= radius; r += radius / particleAmount) {
            for (double t = 0; t < 2 * Math.PI; t += Math.PI / particleAmount) {
                double x = r * Math.cos(t);
                double z = r * Math.sin(t);
                Vector vec = new Vector(x, 0, z);
                CruxLocation location = center.value();
                vec.rotateAroundX(Math.toRadians(location.pitch()));
                vec.rotateAroundY(Math.toRadians(location.yaw() * -1));
                CruxLocation result = location.add(vec.getX(), vec.getY(), vec.getZ());
                consumer.accept(result);
            }
        }
    }

    public List<Vector> generateVectors(){
        List<Vector> list = new ArrayList<>();
        double radius = this.radius.value().doubleValue();
        int parAmount = (int) (8f * radius);
        parAmount *= (int) amountMultiplier.value().doubleValue();

        int particleAmount = parAmount / 2;
        int iterations = parAmount;

        switch (type){
            case WHOLE -> {
                for (double r = 0; r <= radius; r += radius / particleAmount) {
                    for (double t = 0; t < 2 * Math.PI; t += Math.PI / particleAmount) {
                        double x = r * Math.cos(t);
                        double z = r * Math.sin(t);
                        Vector vec = new Vector(x, 0, z);
                        list.add(vec);
                    }
                }
            }
            case HOLLOW -> {
                double t = 0D;
                for(; iterations > 0; iterations--){
                    t = t + Math.PI / particleAmount;
                    double x = radius * Math.cos(t);
                    double z = radius * Math.sin(t);
                    Vector vec = new Vector(x, 0, z);
                    list.add(vec);
                }
            }
        }
        return list;
    }

    @Override
    public void generate(@NotNull Consumer<CruxLocation> consumer) {
        generateVectors().forEach(vec ->{
            CruxLocation location = center.value();
            vec.rotateAroundX(Math.toRadians(location.pitch()));
            vec.rotateAroundY(Math.toRadians(location.yaw()*-1));

            CruxLocation result = location.add(vec.getX(), vec.getY(), vec.getZ());
            consumer.accept(result);
        });
        /*double radius = this.radius.value().doubleValue();
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
            vec.rotateAroundX(Math.toRadians(location.pitch()));
            vec.rotateAroundY(Math.toRadians(location.yaw()*-1));

            CruxLocation result = location.add(vec.getX(), vec.getY(), vec.getZ());
            consumer.accept(result);
        }*/
    }

    @Override
    public @NotNull CreateCachedShape generateCache(@Nullable Consumer<Vector> consumer) {
        return new SimpleCachedCircle(generateVectors(), center);
    }

    public static class Builder implements CreateCircle.Builder{
        private Holder<CruxLocation> center;
        private NumberProvider radius;
        private NumberProvider amountMultiplier;
        private boolean invertX;
        private boolean invertZ;
        private CreateCircle.Type type = Type.HOLLOW;

        @Override
        public Builder type(CreateCircle.Type type){
            this.type = type;
            return this;
        }

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
            if(amountMultiplier == null) amountMultiplier = NumberProvider.constant(1);
            if(radius == null) radius = NumberProvider.constant(5);
            return new SimpleCreateCircle(center, radius, amountMultiplier, type, invertX, invertZ);
        }
    }
}
