package killercreepr.crux.valueproviders.number;

import killercreepr.crux.context.InputContext;
import killercreepr.crux.util.CruxMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RangeNumber implements NumberProvider {
    protected final @NotNull NumberProvider min;
    protected final @NotNull NumberProvider max;

    public RangeNumber(@NotNull NumberProvider min, @NotNull NumberProvider max) {
        this.min = min;
        this.max = max;
    }

    public RangeNumber(@NotNull Number min, @NotNull Number max) {
        this(new ConstantNumber(min), new ConstantNumber(max));
    }

    @Override
    public @NotNull List<Number> sampleList(@NotNull Random random, @Nullable InputContext ev) {
        List<Number> list = new ArrayList<>();
        int min = this.min.sample(random, ev).intValue();
        int max = this.max.sample(random, ev).intValue();
        for(int i = min; i <= max; i++){
            list.add(i);
        }
        return list;
    }

    public @NotNull NumberProvider getMin() {
        return min;
    }

    public @NotNull NumberProvider getMax() {
        return max;
    }

    @Override
    public @NotNull Number sample(@NotNull Random random, @Nullable InputContext ev) {
        return CruxMath.random(min.sample(random, ev).doubleValue(), max.sample(random, ev).doubleValue(), random);
    }

    @Override
    public @NotNull Number getMinValue() {
        return min.value();
    }

    @Override
    public @NotNull Number getMaxValue() {
        return max.value();
    }
    @Override
    public String toString() {
        return "RangeNumber{min=" + min + ", max=" + max + "}";
    }
}
