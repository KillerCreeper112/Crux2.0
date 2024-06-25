package killercreepr.crux.valueproviders.number;

import killercreepr.crux.context.InputContext;
import killercreepr.crux.util.CruxMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class EquationNumber implements NumberProvider{
    protected final @NotNull String equation;
    public EquationNumber(@NotNull String equation) {
        this.equation = equation;
    }

    public @NotNull String getEquation() {
        return equation;
    }

    @Override
    public @NotNull Number getMinValue() {
        return value();
    }

    @Override
    public @NotNull Number getMaxValue() {
        return value();
    }

    @Override
    public @NotNull Number sample(@NotNull Random random, @Nullable InputContext context) {
        if(context == null) return CruxMath.evaluate(equation);
        return CruxMath.evaluate(context.input(equation));
    }

    @Override
    public String toString() {
        return "EquationNumber{equation=" + equation + "}";
    }
}
