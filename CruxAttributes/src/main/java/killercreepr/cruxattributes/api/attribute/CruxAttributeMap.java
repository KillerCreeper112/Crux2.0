package killercreepr.cruxattributes.api.attribute;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiConsumer;

public interface CruxAttributeMap {
    double getValue(@NotNull CruxAttribute attribute);
    boolean hasAttribute(@NotNull CruxAttribute attribute);
    @Contract(pure = true)
    CruxAttributeMap setValue(@NotNull CruxAttribute attribute, double value);
    @Contract(pure = true)
    CruxAttributeMap removeValue(@NotNull CruxAttribute attribute);
    @NotNull Map<CruxAttribute, Double> getMap();
    CruxAttributeMap forEach(BiConsumer<CruxAttribute, Double> consumer);
}
