package killercreepr.crux.valueproviders.number;

import killercreepr.crux.util.CruxMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redempt.crunch.functional.EvaluationEnvironment;

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
    public @NotNull Number sample(@NotNull Random random) {
        return CruxMath.evaluate(equation);
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
    public @NotNull Number value(@Nullable EvaluationEnvironment ev) {
        return CruxMath.evaluate(equation, ev);
    }

    @Override
    public String toString() {
        return "EquationNumber{equation=" + equation + "}";
    }
}
