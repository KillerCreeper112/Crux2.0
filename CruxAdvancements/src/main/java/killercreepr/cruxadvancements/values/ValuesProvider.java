package killercreepr.cruxadvancements.values;

import killercreepr.crux.data.Reloadable;
import killercreepr.crux.valueproviders.number.NumberProvider;
import org.jetbrains.annotations.NotNull;

public interface ValuesProvider extends Reloadable {
    @NotNull
    NumberProvider DEFAULT_MAX_TRACKED_ADVANCEMENTS();
}
