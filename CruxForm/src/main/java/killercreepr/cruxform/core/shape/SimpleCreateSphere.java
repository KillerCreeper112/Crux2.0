package killercreepr.cruxform.core.shape;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxform.api.shape.CreateSphere;
import killercreepr.cruxform.api.shape.cache.CreateCachedShape;
import killercreepr.cruxform.core.shape.cache.SimpleCachedCircle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SimpleCreateSphere implements CreateSphere {
    protected final Holder<CruxLocation> center;
    protected final NumberProvider radius;
    protected final NumberProvider spacing;
    protected final Type type;
    protected final boolean invertX;
    protected final boolean invertY;
    protected final boolean invertZ;

    public SimpleCreateSphere(Holder<CruxLocation> center,
                              NumberProvider radius,
                              NumberProvider spacing,
                              Type type, boolean invertX, boolean invertY, boolean invertZ) {
        this.center = center;
        this.radius = radius;
        this.spacing = spacing;
        this.type = type;
        this.invertX = invertX;
        this.invertY = invertY;
        this.invertZ = invertZ;
    }

    public List<Vector> generateVectors(){
        List<Vector> list = new ArrayList<>();
        double spacing = this.spacing.value().doubleValue();
        double radius = this.radius.value().doubleValue();
        final double newSpacing = spacing == 0f ? (float) (radius * 3f) + 1f : spacing;

        int invertX = this.invertX ? -1 : 1;
        int invertY = this.invertY ? -1 : 1;
        int invertZ = this.invertZ ? -1 : 1;

        switch (type){
            case WHOLE -> {
                for (double i = 0; i <= Math.PI; i += Math.PI / newSpacing) {
                    for (double a = 0; a < Math.PI * 2; a += Math.PI / newSpacing) {

                        // Randomly choose a radial distance between 0 and the radius
                        double r = radius * Math.cbrt(Math.random()); // Cube root to keep the distribution uniform

                        // Convert spherical coordinates (r, i, a) to Cartesian coordinates (x, y, z)
                        double x = r * Math.sin(i) * Math.cos(a) * invertX;
                        double y = r * Math.cos(i) * invertY;
                        double z = r * Math.sin(i) * Math.sin(a) * invertZ;

                        // Create vector and add it to the list
                        Vector vec = new Vector(x, y, z);
                        list.add(vec);
                    }
                }
            }
            case HOLLOW -> {
                for (double i = 0; i <= Math.PI; i += Math.PI / newSpacing) {
                    double r = Math.sin(i);
                    double y = radius * Math.cos(i) * invertY;
                    for (double a = 0; a < Math.PI * 2; a += Math.PI / newSpacing) {
                        double x = radius * Math.cos(a) * r * invertX;
                        double z = radius * Math.sin(a) * r * invertZ;
                        Vector vec = new Vector(x, y, z);
                        list.add(vec);
                    }
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
    }

    @Override
    public @NotNull CreateCachedShape generateCache(@Nullable Consumer<Vector> consumer) {
        return new SimpleCachedCircle(generateVectors(), center);
    }

    public static class Builder implements CreateSphere.Builder{
        private Holder<CruxLocation> center;
        private NumberProvider radius;
        private NumberProvider spacing;
        private boolean invertX;
        private boolean invertY;
        private boolean invertZ;
        private Type type = Type.HOLLOW;

        @Override
        public Builder type(Type type){
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
        public Builder spacing(NumberProvider spacing) {
            this.spacing = spacing;
            return this;
        }

        @Override
        public Builder invertX(boolean invertX) {
            this.invertX = invertX;
            return this;
        }

        @Override
        public CreateSphere.Builder invertY(boolean invertY) {
            this.invertY = invertY;
            return this;
        }

        @Override
        public Builder invertZ(boolean invertZ) {
            this.invertZ = invertZ;
            return this;
        }

        @Override
        public CreateSphere build() {
            if(spacing == null) spacing = NumberProvider.constant(0);
            if(radius == null) radius = NumberProvider.constant(5);
            return new SimpleCreateSphere(center, radius, spacing, type, invertX, invertY, invertZ);
        }
    }
}
