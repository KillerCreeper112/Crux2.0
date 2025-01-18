package killercreepr.crux.core.valueproviders.number;

import killercreepr.crux.api.text.context.InputContext;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Random;

public class ConstantNumber implements NumberProvider {
    protected final @NotNull Number constant;
    public ConstantNumber(@NotNull Number constant) {
        this.constant = constant;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(constant);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof ConstantNumber n)) return false;
        return Objects.equals(n.constant, constant);
    }

    public @NotNull Number getConstant() {
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
    public @NotNull Number sample(@NotNull Random random, @Nullable InputContext ev) {
        return constant;
    }

    @Override
    public String toString() {
        return "ConstantNumber{value=" + constant + "}";
    }
}
