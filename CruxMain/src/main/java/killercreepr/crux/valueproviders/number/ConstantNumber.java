package killercreepr.crux.valueproviders.number;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class ConstantNumber implements NumberProvider{
    protected final @NotNull Number constant;
    public ConstantNumber(@NotNull Number constant) {
        this.constant = constant;
    }

    public @NotNull Number getConstant() {
        return constant;
    }

    @Override
    public @NotNull Number sample(@NotNull Random random) {
        return constant;
    }

    @Override
    public @NotNull Number getMinValue() {
        return constant;
    }

    @Override
    public @NotNull Number getMaxValue() {
        return constant;
    }

    @Override
    public String toString() {
        return "ConstantNumber{value=" + constant + "}";
    }
}
