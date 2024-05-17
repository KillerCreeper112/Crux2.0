package killercreepr.crux.valueproviders.number;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redempt.crunch.functional.EvaluationEnvironment;

import java.util.Random;

public interface NumberProvider extends NumberHolder {
    @NotNull Number sample(@NotNull Random random);
    default @NotNull Number sample(){ return sample(new Random()); }

    @NotNull Number getMinValue();
    @NotNull Number getMaxValue();
    @Override
    default @NotNull Number value(){ return value(null); }

    /**
     * @return Used primarily for equation numbers to allow for variables to be replaced with certain numbers.
     */
    default @NotNull Number value(@Nullable EvaluationEnvironment ev){
        return sample();
    }
}
