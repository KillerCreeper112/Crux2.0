package killercreepr.crux.valueproviders.vector;

import killercreepr.crux.valueproviders.number.NumberProvider;
import org.jetbrains.annotations.NotNull;

public interface NumberVector {
    @NotNull
    NumberProvider x();
    @NotNull
    NumberProvider y();
    @NotNull
    NumberProvider z();
}
