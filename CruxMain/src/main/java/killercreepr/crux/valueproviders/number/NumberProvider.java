package killercreepr.crux.valueproviders.number;

import killercreepr.crux.context.InputContext;
import killercreepr.crux.util.CruxMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public interface NumberProvider extends NumberHolder {
    @NotNull Number getMinValue();
    @NotNull Number getMaxValue();
    @Override
    default @NotNull Number value(){ return sample(); }

    /**
     * @return Used primarily for equation numbers to allow for variables to be replaced with certain numbers.
     */
    @NotNull Number sample(@NotNull Random random, @Nullable InputContext ev);

    default @NotNull Number sample(@Nullable InputContext ev){
        return sample(CruxMath.RANDOM, ev);
    }
    default @NotNull Number sample(@NotNull Random random){
        return sample(random, null);
    }
    default @NotNull Number sample(){ return sample(CruxMath.RANDOM); }
}
