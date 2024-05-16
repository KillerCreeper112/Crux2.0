package killercreepr.crux.valueproviders.number;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public interface NumberProvider extends NumberHolder {
    @NotNull Number sample(@NotNull Random random);
    default @NotNull Number sample(){ return sample(new Random()); }

    @NotNull Number getMinValue();
    @NotNull Number getMaxValue();

    @Override
    default @Nullable Number value(){ return sample(); }
}
