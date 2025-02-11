package killercreepr.cruxattributes.api.attribute;

import killercreepr.cruxattributes.core.attribute.map.SimpleCruxAttributeMap;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiConsumer;

public interface CruxAttributeMap {
    static CruxAttributeMap emptyMap(){
        return EMPTY;
    }
    static CruxAttributeMap attributeMap(Map<CruxAttribute, Double> map){
        return new SimpleCruxAttributeMap(map);
    }

    static CruxAttributeMap attributeMap(CruxAttribute attribute, double value){
        return attributeMap(Map.of(attribute, value));
    }

    CruxAttributeMap EMPTY = new SimpleCruxAttributeMap(Map.of());

    double getValue(@NotNull CruxAttribute attribute);
    boolean hasAttribute(@NotNull CruxAttribute attribute);
    @Contract(pure = true)
    CruxAttributeMap setValue(@NotNull CruxAttribute attribute, double value);
    @Contract(pure = true)
    CruxAttributeMap removeValue(@NotNull CruxAttribute attribute);
    @NotNull Map<CruxAttribute, Double> getMap();
    CruxAttributeMap forEach(BiConsumer<CruxAttribute, Double> consumer);
}
