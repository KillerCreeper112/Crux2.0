package killercreepr.crux.core.valueproviders.vector;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.api.valueproviders.vector.NumberVector;
import org.jetbrains.annotations.NotNull;

public class SimpleNumberVector implements NumberVector {
    protected final @NotNull NumberProvider x;
    protected final @NotNull NumberProvider y;
    protected final @NotNull NumberProvider z;

    public SimpleNumberVector(@NotNull NumberProvider x, @NotNull NumberProvider y, @NotNull NumberProvider z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public @NotNull NumberProvider x() {
        return x;
    }

    @Override
    public @NotNull NumberProvider y() {
        return y;
    }

    @Override
    public @NotNull NumberProvider z() {
        return z;
    }
}
