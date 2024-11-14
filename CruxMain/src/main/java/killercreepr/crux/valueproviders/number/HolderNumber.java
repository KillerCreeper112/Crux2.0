package killercreepr.crux.valueproviders.number;

import killercreepr.crux.context.InputContext;
import killercreepr.crux.data.Holder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class HolderNumber implements NumberProvider{
    protected final @NotNull Holder<Number> holder;

    public HolderNumber(@NotNull Holder<Number> holder) {
        this.holder = holder;
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
    public @NotNull Number sample(@NotNull Random random, @Nullable InputContext ev) {
        return holder.value();
    }
}
