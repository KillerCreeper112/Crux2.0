package killercreepr.crux.valueproviders.random;

import killercreepr.crux.data.Holder;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public interface RandomHolder extends Holder<Random> {
    @Override
    @NotNull Random value();
}
