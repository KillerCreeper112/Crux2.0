package killercreepr.cruxattributes.api.attribute;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface CruxAttributeAccessor {
    double getValue(@NotNull CruxAttribute attribute);
    double getValueOrDefault(@NotNull CruxAttribute attribute, double fallback);
    @Nullable CruxAttributeInstance getInstance(@NotNull CruxAttribute attribute);
    @NotNull Collection<? extends CruxAttributeInstance> getInstances();
    @Contract(pure = true)
    @NotNull CruxAttributeAccessor copy();
}
