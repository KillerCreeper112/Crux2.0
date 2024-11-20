package killercreepr.cruxadvancements.core.values;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxadvancements.api.values.ValuesProvider;
import org.jetbrains.annotations.NotNull;

public class DefaultValues implements ValuesProvider {
    public static final NumberProvider DEFAULT_MAX_TRACKED_ADVANCEMENTS = NumberProvider.constant(3);
    @Override
    public @NotNull NumberProvider DEFAULT_MAX_TRACKED_ADVANCEMENTS() {
        return DEFAULT_MAX_TRACKED_ADVANCEMENTS;
    }
}
