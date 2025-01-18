package killercreepr.crux.core.data;

import com.destroystokyo.paper.ParticleBuilder;
import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.data.ParticleBuilderSupplier;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.api.valueproviders.vector.NumberVector;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SimpleParticleBuilderSupplier implements ParticleBuilderSupplier {
    protected final Holder<Particle> particle;
    protected final NumberProvider count;
    protected final NumberVector offset;
    protected final NumberProvider extra;
    protected final Holder<Object> data;

    public SimpleParticleBuilderSupplier(Holder<Particle> particle, NumberProvider count, NumberVector offset, NumberProvider extra, Holder<Object> data) {
        this.particle = particle;
        this.count = count;
        this.offset = offset;
        this.extra = extra;
        this.data = data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            particle, count, offset, extra, data
        );
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof SimpleParticleBuilderSupplier other)) return false;
        return particle.equals(other.particle) &&
            count.equals(other.count) &&
            offset.equals(other.offset) &&
            extra.equals(other.extra) &&
            data.equals(other.data);
    }

    @Override
    public Holder<Particle> particle() {
        return particle;
    }

    @Override
    public NumberProvider count() {
        return count;
    }

    @Override
    public NumberVector offset() {
        return offset;
    }

    @Override
    public NumberProvider extra() {
        return extra;
    }

    @Override
    public Holder<Object> data() {
        return data;
    }

    @Override
    public Object openData() {
        return data == null ? null : data.value();
    }

    @Override
    public @NotNull ParticleBuilder build() {
        ParticleBuilder build = new ParticleBuilder(particle.value())
            .offset(0, 0, 0)
            .extra(0);
        if(count != null){
            build.count(count.value().intValue());
        }
        if(offset != null){
            build.offset(
                offset.x().value().doubleValue(),
                offset.y().value().doubleValue(),
                offset.z().value().doubleValue()
            );
        }
        if(extra != null){
            build.extra(extra.value().doubleValue());
        }
        if(data != null){
            build.data(data.value());
        }
        return build;
    }

    public static class Builder implements ParticleBuilderSupplier.Builder{
        private Holder<Particle> particle;
        private NumberProvider count;
        private NumberVector offset;
        private NumberProvider extra;
        private Holder<Object> data;

        @Override
        public Builder particle(Holder<Particle> particle) {
            this.particle = particle;
            return this;
        }

        @Override
        public Builder count(NumberProvider count) {
            this.count = count;
            return this;
        }

        @Override
        public Builder offset(NumberVector offset) {
            this.offset = offset;
            return this;
        }

        @Override
        public Builder extra(NumberProvider extra) {
            this.extra = extra;
            return this;
        }

        @Override
        public Builder data(Holder<Object> data) {
            this.data = data;
            return this;
        }

        @Override
        public ParticleBuilderSupplier build() {
            return new SimpleParticleBuilderSupplier(
                particle, count, offset, extra, data
            );
        }
    }
}
