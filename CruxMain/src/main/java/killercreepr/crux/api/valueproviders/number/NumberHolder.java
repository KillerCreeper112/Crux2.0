package killercreepr.crux.api.valueproviders.number;

import killercreepr.crux.api.data.Holder;
import org.jetbrains.annotations.NotNull;

public interface NumberHolder extends Holder<Number> {
    @Override
    @NotNull Number value();
}
