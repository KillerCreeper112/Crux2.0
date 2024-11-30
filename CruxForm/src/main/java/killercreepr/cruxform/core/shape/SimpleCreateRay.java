package killercreepr.cruxform.core.shape;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxform.api.shape.CreateRay;
import killercreepr.cruxform.api.shape.cache.CreateCachedShape;
import killercreepr.cruxform.core.shape.cache.SimpleCachedAddRelative;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SimpleCreateRay implements CreateRay {
    protected final Holder<CruxLocation> start;
    protected final NumberProvider length;
    protected final NumberProvider spacing;

    public SimpleCreateRay(Holder<CruxLocation> start, NumberProvider length, NumberProvider spacing) {
        this.start = start;
        this.length = length;
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
        double spacing = this.spacing.value().doubleValue();
        double length = this.length.value().doubleValue();

        for(double currentLength = 0f; currentLength <= length; currentLength += spacing){
            Vector vec = new Vector(currentLength, 0f, 0f);
            list.add(vec);
        }
        return list;
    }

    @Override
    public @NotNull CreateCachedShape generateCache(@Nullable Consumer<Vector> consumer) {
        return new SimpleCachedAddRelative(generateVectors(), start);
    }
    public static class Builder implements CreateRay.Builder {

        private Holder<CruxLocation> start;
        private NumberProvider length;
        private NumberProvider spacing;

        @Override
        public Builder start(Holder<CruxLocation> start) {
            this.start = start;
            return this;
        }

        @Override
        public Builder length(NumberProvider length) {
            this.length = length;
            return this;
        }

        @Override
        public CreateRay build() {
            return new SimpleCreateRay(start, length, spacing);
        }

        // Default method implementations
        public Builder start(CruxLocation start) {
            return start(Holder.direct(start));
        }

        public Builder length(double length) {
            return length(NumberProvider.constant(length));
        }

        @Override
        public CreateRay.Builder spacing(NumberProvider spacing) {
            this.spacing = spacing;
            return this;
        }
    }
}
