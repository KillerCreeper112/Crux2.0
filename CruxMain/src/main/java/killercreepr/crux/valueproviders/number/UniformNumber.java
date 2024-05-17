package killercreepr.crux.valueproviders.number;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redempt.crunch.functional.EvaluationEnvironment;

import java.util.Random;

public class UniformNumber implements NumberProvider {
    protected final @NotNull NumberProvider minInclusive;
    protected final @NotNull NumberProvider maxInclusive;
    public UniformNumber(@NotNull Number min, @NotNull Number max) {
        this(new ConstantNumber(min), new ConstantNumber(max));
    }

    public UniformNumber(@NotNull NumberProvider min, @NotNull NumberProvider max) {
        this.minInclusive = min;
        this.maxInclusive = max;
    }

    @Override
    public @NotNull Number sample(@NotNull Random random) {
        return sample(random, minInclusive.value(), maxInclusive.value());
    }

    public @NotNull Number sample(@NotNull Random random, @NotNull Number minInclusive, @NotNull Number maxInclusive) {
        if (maxInclusive instanceof Integer) return random.nextInt((int) maxInclusive - (int) minInclusive + 1) + (int) minInclusive;
        if (maxInclusive instanceof Double) return random.nextDouble() * ((double) maxInclusive - (double) minInclusive) + (double) minInclusive;
        if (maxInclusive instanceof Float) return random.nextFloat() * ((float) maxInclusive - (float) minInclusive) + (float) minInclusive;
        if (maxInclusive instanceof Long) return random.nextLong() % ((long) maxInclusive - (long) minInclusive + 1) + (long) minInclusive;
        if (maxInclusive instanceof Short) return (short) (random.nextInt((short) maxInclusive - (short) minInclusive + 1) + (short) minInclusive);
        throw new UnsupportedOperationException("Unsupported Number type");
    }

    @Override
    public @NotNull Number value(@Nullable EvaluationEnvironment ev) {
        return sample(new Random(), minInclusive.value(ev), maxInclusive.value(ev));
    }

    public @NotNull NumberProvider getMinInclusive() {
        return minInclusive;
    }

    public @NotNull NumberProvider getMaxInclusive() {
        return maxInclusive;
    }

    @Override
    public @NotNull Number getMinValue() {
        return this.minInclusive.value();
    }

    @Override
    public @NotNull Number getMaxValue() {
        return this.maxInclusive.value();
    }
    @Override
    public String toString() {
        return "[" + this.minInclusive + "-" + this.maxInclusive + "]";
    }
}
