package killercreepr.cruxform.core.shape;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxform.api.shape.CreateRectangle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SimpleCreateRectangle implements CreateRectangle {
    protected final Holder<CruxLocation> pos1;
    protected final Holder<CruxLocation> pos2;
    protected final CreateRectangle.Type type;
    protected final NumberProvider spacing;
    protected final NumberProvider time;
    protected final boolean inverted;

    public SimpleCreateRectangle(Holder<CruxLocation> pos1, Holder<CruxLocation> pos2,
                                 CreateRectangle.Type type,
                                 NumberProvider spacing, NumberProvider time,
                                 boolean inverted) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.type = type;
        this.spacing = spacing;
        this.time = time;
        this.inverted = inverted;
    }

    @Override
    public void generate(@NotNull Consumer<CruxLocation> consumer) {
        CruxLocation pos1Pos = pos1.value();
        CruxLocation pos2Pos = pos2.value();
        final CruxLocation max = CruxLocation.getMaximum(pos1Pos, pos2Pos);
        final CruxLocation min = CruxLocation.getMinimum(pos1Pos, pos2Pos);
        double spacing = this.spacing.value().doubleValue();

        final int xParticleAmount = Math.max(1, (int) (((max.x()-min.x()) / spacing)));
        final int yParticleAmount = Math.max(1, (int) (((max.y()-min.y()) / spacing)));
        final int zParticleAmount = Math.max(1, (int) (((max.z()-min.z()) / spacing)));

        for(int x = 0; x <= xParticleAmount; x++){
            for(int y = 0; y <= yParticleAmount; y++){
                for(int z = 0; z <= zParticleAmount; z++){
                    switch (type){
                        case WIRE -> {
                            if(!(((x == 0 || x == xParticleAmount)) &&
                                ((y == 0 || y == yParticleAmount)) ||
                                ((x == 0 || x == xParticleAmount)) &&
                                    ((z == 0 || z == zParticleAmount)) ||
                                ((y == 0 || y == yParticleAmount)) &&
                                    ((z == 0 || z == zParticleAmount)))) continue;
                        }
                        case HOLLOW -> {
                            if (!(x == 0 || x == xParticleAmount || y == 0 ||
                                y == yParticleAmount || z == 0 || z == zParticleAmount)) continue;
                        }
                        case WALLS -> {
                            if (!(x == 0 || x == xParticleAmount || z == 0 || z == zParticleAmount)) continue;
                            if (!(y >= 0 && y <= yParticleAmount)) continue;
                        }
                    }
                    Vector vec = new Vector((max.x() - min.x()) * ((double) x /xParticleAmount),
                        (max.y() - min.y()) * ((double) y /yParticleAmount),
                        (max.z() - min.z()) * ((double) z /zParticleAmount));

                    CruxLocation result = min.add(vec.getX(), vec.getY(), vec.getZ());
                    consumer.accept(result);
                }
            }
        }
    }

    public static class Builder implements CreateRectangle.Builder {
        protected Holder<CruxLocation> pos1;
        protected Holder<CruxLocation> pos2;
        protected CreateRectangle.Type type;
        protected NumberProvider spacing;
        protected NumberProvider time;
        protected boolean inverted;

        @Override
        public Builder pos1(Holder<CruxLocation> pos1) {
            this.pos1 = pos1;
            return this; // Enables fluent interface
        }

        @Override
        public Builder pos2(Holder<CruxLocation> pos2) {
            this.pos2 = pos2;
            return this;
        }

        @Override
        public Builder type(CreateRectangle.Type type) {
            this.type = type;
            return this;
        }

        @Override
        public Builder spacing(NumberProvider spacing) {
            this.spacing = spacing;
            return this;
        }

        @Override
        public Builder time(NumberProvider time) {
            this.time = time;
            return this;
        }

        @Override
        public Builder inverted(boolean inverted) {
            this.inverted = inverted;
            return this;
        }

        @Override
        public CreateRectangle build() {
            return new SimpleCreateRectangle(pos1, pos2, type, spacing, time, inverted);
        }
    }
}
