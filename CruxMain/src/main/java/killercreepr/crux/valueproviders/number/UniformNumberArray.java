package killercreepr.crux.valueproviders.number;

import com.google.common.base.Preconditions;
import killercreepr.crux.valueproviders.InputContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redempt.crunch.functional.EvaluationEnvironment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class UniformNumberArray implements NumberProvider {
    protected final @NotNull NumberProvider @NotNull[] numbers;

    public UniformNumberArray(@NotNull Number @NotNull[] numbers) {
        Preconditions.checkArgument(numbers.length > 0,
                "Uniform number array must have at least one value!");
        List<NumberProvider> list = new ArrayList<>();
        for(Number n : numbers){
            list.add(new ConstantNumber(n));
        }
        this.numbers = list.toArray(new NumberProvider[0]);
    }

    public UniformNumberArray(@NotNull NumberProvider @NotNull[] numbers) {
        Preconditions.checkArgument(numbers.length > 0,
                "Uniform number array must have at least one value!");
        this.numbers = numbers;
    }

    public @NotNull NumberProvider @NotNull[] getNumbers() {
        return numbers;
    }

    @Override
    public @NotNull Number value(@Nullable InputContext ev) {
        return sampleProvider(new Random()).value(ev);
    }

    public @NotNull NumberProvider sampleProvider(@NotNull Random random){
        int randomIndex = random.nextInt(numbers.length);
        return numbers[randomIndex];
    }

    @Override
    public @NotNull Number sample(@NotNull Random random) {
        return sampleProvider(random).sample(random);
    }

    @Override
    public @NotNull Number getMinValue() {
        Number min = numbers[0].value();
        for (NumberProvider num : numbers) {
            double x = num.value().doubleValue();
            if (x < min.doubleValue()) min = x;
        }
        return min;
    }

    @Override
    public @NotNull Number getMaxValue() {
        Number max = numbers[0].value();;
        for (NumberProvider num : numbers) {
            double x = num.value().doubleValue();
            if (x > max.doubleValue()) max = x;
        }
        return max;
    }
    @Override
    public String toString() {
        return "UniformNumberArray{values=" + Arrays.toString(numbers) + "}";
    }
}
