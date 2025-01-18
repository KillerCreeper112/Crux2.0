package killercreepr.crux.api.data;

import com.destroystokyo.paper.ParticleBuilder;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.api.valueproviders.vector.NumberVector;
import killercreepr.crux.core.data.SimpleParticleBuilderSupplier;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public interface ParticleBuilderSupplier {
    static Builder builder(){
        return new SimpleParticleBuilderSupplier.Builder();
    }

    Holder<Particle> particle();
    NumberProvider count();
    NumberVector offset();
    NumberProvider extra();
    Holder<Object> data();
    Object openData();
    @NotNull ParticleBuilder build();

    interface Builder{
        Builder particle(Holder<Particle> particle);
        default Builder particle(Particle particle){
            return particle(Holder.direct(particle));
        }
        default Builder count(int amount){
            return count(NumberProvider.constant(amount));
        }
        default Builder offset(double x, double y, double z){
            return offset(NumberVector.vector(x, y, z));
        }
        default Builder extra(double extra){
            return extra(NumberProvider.constant(extra));
        }
        default Builder data(Object extra){
            return data(extra == null ? null : Holder.direct(extra));
        }
        Builder count(NumberProvider count);
        Builder offset(NumberVector offset);
        Builder extra(NumberProvider extra);
        Builder data(Holder<Object> data);
        ParticleBuilderSupplier build();
    }
}
