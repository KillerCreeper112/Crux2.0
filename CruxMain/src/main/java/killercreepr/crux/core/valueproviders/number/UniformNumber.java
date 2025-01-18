package killercreepr.crux.core.valueproviders.number;

import killercreepr.crux.api.text.context.InputContext;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    public int hashCode() {
        return Objects.hash(minInclusive, maxInclusive);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof UniformNumber n)) return false;
        return Objects.equals(n.minInclusive, minInclusive) && Objects.equals(n.maxInclusive, maxInclusive);
    }

    public @NotNull Number sample(@NotNull Random random, @NotNull Number minInclusive, @NotNull Number maxInclusive) {
        return switch (maxInclusive) {
            case Integer v -> random.nextInt((int) maxInclusive - (int) minInclusive + 1) + (int) minInclusive;
            case Double v ->
                random.nextDouble() * ((double) maxInclusive - (double) minInclusive) + (double) minInclusive;
            case Float v -> random.nextFloat() * ((float) maxInclusive - (float) minInclusive) + (float) minInclusive;
            case Long v -> random.nextLong() % ((long) maxInclusive - (long) minInclusive + 1) + (long) minInclusive;
            case Short v ->
                (short) (random.nextInt((short) maxInclusive - (short) minInclusive + 1) + (short) minInclusive);
            default ->
                throw new UnsupportedOperationException("Unsupported Number type " + maxInclusive.getClass().getSimpleName() + " / " + minInclusive.getClass().getSimpleName());
        };
    }

    @Override
    public @NotNull List<Number> sampleList(@NotNull Random random, @Nullable InputContext ev){
        List<Number> list = new ArrayList<>();

        Number minInclusive = this.minInclusive.sample(random, ev);
        Number maxInclusive = this.maxInclusive.sample(random, ev);
        switch (maxInclusive) {
            case Integer v -> {
                int min = minInclusive.intValue();
                int max = maxInclusive.intValue();
                for (int i = min; i <= max; i++) {
                    list.add(i);
                }
                return list;
            }
            case Double v -> {
                double min = minInclusive.doubleValue();
                double max = maxInclusive.doubleValue();
                for (double i = min; i <= max; i++) {
                    list.add(i);
                }
                return list;
            }
            case Float v -> {
                float min = minInclusive.floatValue();
                float max = maxInclusive.floatValue();
                for (float i = min; i <= max; i++) {
                    list.add(i);
                }
                return list;
            }
            case Long v -> {
                long min = minInclusive.intValue();
                long max = maxInclusive.intValue();
                for (long i = min; i <= max; i++) {
                    list.add(i);
                }
                return list;
            }
            case Short v -> {
                short min = minInclusive.shortValue();
                short max = maxInclusive.shortValue();
                for (short i = min; i <= max; i++) {
                    list.add(i);
                }
                return list;
            }
            default -> {
            }
        }
        throw new UnsupportedOperationException("Unsupported Number type " + maxInclusive.getClass().getSimpleName() + " / " + minInclusive.getClass().getSimpleName());
    }

    @Override
    public @NotNull Number sample(@NotNull Random random, @Nullable InputContext ev) {
        return sample(random, minInclusive.sample(random, ev), maxInclusive.sample(random, ev));
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
        return "UniformNumber{min=" + this.minInclusive + ", max=" + this.maxInclusive + "}";
    }
}
