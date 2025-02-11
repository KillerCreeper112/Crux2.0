package killercreepr.cruxattributes.core.attribute.map;

import com.google.common.collect.ImmutableMap;
import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.attribute.CruxAttributeMap;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class SimpleCruxAttributeMap implements CruxAttributeMap {
    protected final Map<CruxAttribute, Double> map;

    public SimpleCruxAttributeMap(Map<CruxAttribute, Double> map) {
        this.map = ImmutableMap.copyOf(map);
    }

    @Override
    public double getValue(@NotNull CruxAttribute attribute) {
        return map.getOrDefault(attribute, 0D);
    }

    @Override
    public boolean hasAttribute(@NotNull CruxAttribute attribute) {
        return map.containsKey(attribute);
    }

    @Override
    public CruxAttributeMap setValue(@NotNull CruxAttribute attribute, double value) {
        Map<CruxAttribute, Double> map = new HashMap<>(this.map);
        map.put(attribute, value);
        return new SimpleCruxAttributeMap(map);
    }

    @Override
    public CruxAttributeMap removeValue(@NotNull CruxAttribute attribute) {
        Map<CruxAttribute, Double> map = new HashMap<>(this.map);
        map.remove(attribute);
        return new SimpleCruxAttributeMap(map);
    }

    @Override
    public @NotNull Map<CruxAttribute, Double> getMap() {
        return map;
    }

    @Override
    public CruxAttributeMap forEach(BiConsumer<CruxAttribute, Double> consumer) {
        map.forEach(consumer);
        return this;
    }
}
