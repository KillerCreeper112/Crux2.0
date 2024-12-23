package killercreepr.cruxform.core.shape;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxform.api.shape.CreateRectangle;
import killercreepr.cruxform.api.shape.cache.CreateCachedShape;
import killercreepr.cruxform.core.shape.cache.SimpleCachedAddCenter;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SimpleCreateRectangle implements CreateRectangle {
    protected final Holder<CruxPosition> pos1;
    protected final Holder<CruxPosition> pos2;
    protected final CreateRectangle.Type type;
    protected final NumberProvider spacing;
    protected final boolean inverted;

    public SimpleCreateRectangle(Holder<CruxPosition> pos1, Holder<CruxPosition> pos2,
                                 CreateRectangle.Type type,
                                 NumberProvider spacing,
                                 boolean inverted) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.type = type;
        this.spacing = spacing;
        this.inverted = inverted;
    }

    public List<Vector> generateVectors(){
        List<Vector> list = new ArrayList<>();
        CruxPosition pos1Pos = pos1.value();
        CruxPosition pos2Pos = pos2.value();
        final CruxPosition max = CruxPosition.getMaximum(pos1Pos, pos2Pos);
        final CruxPosition min = CruxPosition.getMinimum(pos1Pos, pos2Pos);
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
                    list.add(vec);
                }
            }
        }
        return list;
    }

    @Override
    public void generate(@NotNull Consumer<CruxPosition> consumer) {
        CruxPosition pos1Pos = pos1.value();
        CruxPosition pos2Pos = pos2.value();
        final CruxPosition max = CruxPosition.getMaximum(pos1Pos, pos2Pos);
        final CruxPosition min = CruxPosition.getMinimum(pos1Pos, pos2Pos);
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

                    CruxPosition result = min.add(vec.getX(), vec.getY(), vec.getZ());
                    consumer.accept(result);
                }
            }
        }
    }

    @Override
    public @NotNull CreateCachedShape generateCache(@Nullable Consumer<Vector> consumer) {
        return new SimpleCachedAddCenter(generateVectors(), () ->{
            CruxPosition pos1Pos = pos1.value();
            CruxPosition pos2Pos = pos2.value();
            return CruxPosition.getMinimum(pos1Pos, pos2Pos);
        });
    }

    public static class Builder implements CreateRectangle.Builder {
        protected Holder<CruxPosition> pos1;
        protected Holder<CruxPosition> pos2;
        protected CreateRectangle.Type type = Type.WIRE;
        protected NumberProvider spacing;
        protected boolean inverted;

        @Override
        public Builder pos1(Holder<CruxPosition> pos1) {
            this.pos1 = pos1;
            return this;
        }

        @Override
        public Builder pos2(Holder<CruxPosition> pos2) {
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
        public Builder inverted(boolean inverted) {
            this.inverted = inverted;
            return this;
        }

        @Override
        public CreateRectangle build() {
            if(spacing == null) spacing = NumberProvider.constant(0.5f);
            return new SimpleCreateRectangle(pos1, pos2, type, spacing, inverted);
        }
    }
}
