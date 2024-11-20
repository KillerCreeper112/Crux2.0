package killercreepr.crux.api.valueproviders.vector;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import org.jetbrains.annotations.NotNull;

public interface NumberVector {
    @NotNull
    NumberProvider x();
    @NotNull
    NumberProvider y();
    @NotNull
    NumberProvider z();
}
