package killercreepr.crux.core.valueproviders.number;

import com.google.gson.internal.LazilyParsedNumber;
import killercreepr.crux.api.text.context.InputContext;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.util.CruxMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Random;

public class UniformSkewedNumber extends UniformNumber {
    protected final @NotNull NumberProvider skew;

    public UniformSkewedNumber(@NotNull Number min, @NotNull Number max, @NotNull Number skew) {
        super(min, max);
        this.skew = new ConstantNumber(skew);
    }

    public UniformSkewedNumber(@NotNull NumberProvider min, @NotNull NumberProvider max, @NotNull NumberProvider skew) {
        super(min, max);
        this.skew = skew;
    }

    public @NotNull NumberProvider getSkew() {
        return skew;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minInclusive, maxInclusive, skew);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof UniformSkewedNumber n)) return false;
        return Objects.equals(n.minInclusive, minInclusive) && Objects.equals(n.maxInclusive, maxInclusive) &&
            Objects.equals(n.skew, skew);
    }

    public @NotNull Number sample(@NotNull Random random, @NotNull Number minInclusive, @NotNull Number maxInclusive, double skew) {
        return switch (maxInclusive) {
            case Integer v -> (int) CruxMath.randomSkewed(random, minInclusive.intValue(), maxInclusive.intValue(), skew);
            case Float v -> (float) CruxMath.randomSkewed(random, minInclusive.floatValue(), maxInclusive.floatValue(), skew);
            case Long v -> (long) CruxMath.randomSkewed(random, minInclusive.longValue(), maxInclusive.longValue(), skew);
            case Short v -> (short) CruxMath.randomSkewed(random, minInclusive.shortValue(), maxInclusive.shortValue(), skew);
            default -> (double) CruxMath.randomSkewed(random, minInclusive.doubleValue(), maxInclusive.doubleValue(), skew);
        };
    }

    @Override
    public @NotNull Number sample(@NotNull Random random, @Nullable InputContext ev) {
        return sample(random, minInclusive.sample(random, ev), maxInclusive.sample(random, ev), skew.sample(random, ev).doubleValue());
    }

    @Override
    public String toString() {
        return "UniformSkewedNumber{min=" + this.minInclusive + ", max=" + this.maxInclusive + ", skew=" + this.skew + "}";
    }
}
