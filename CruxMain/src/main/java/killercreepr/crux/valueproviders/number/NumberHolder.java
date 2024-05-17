package killercreepr.crux.valueproviders.number;

import killercreepr.crux.data.Holder;
import org.jetbrains.annotations.NotNull;

public interface NumberHolder extends Holder<Number> {
    @Override
    @NotNull Number value();
}
