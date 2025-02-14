package killercreepr.cruxattributes.api.attribute;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CruxAttributeAccessor {
    double getValue(@NotNull CruxAttribute attribute);
    double getValueOrDefault(@NotNull CruxAttribute attribute, double fallback);
    @Nullable CruxAttributeInstance getInstance(@NotNull CruxAttribute attribute);
}
