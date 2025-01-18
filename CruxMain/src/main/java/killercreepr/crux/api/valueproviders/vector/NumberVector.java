package killercreepr.crux.api.valueproviders.vector;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.valueproviders.vector.SimpleNumberVector;
import org.jetbrains.annotations.NotNull;

public interface NumberVector {
    static NumberVector vector(@NotNull NumberProvider x, @NotNull NumberProvider y, @NotNull NumberProvider z){
        return new SimpleNumberVector(x, y, z);
    }

    @NotNull
    NumberProvider x();
    @NotNull
    NumberProvider y();
    @NotNull
    NumberProvider z();
}
