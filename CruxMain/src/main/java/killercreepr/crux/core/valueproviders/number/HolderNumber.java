package killercreepr.crux.core.valueproviders.number;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.text.context.InputContext;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class HolderNumber implements NumberProvider {
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
