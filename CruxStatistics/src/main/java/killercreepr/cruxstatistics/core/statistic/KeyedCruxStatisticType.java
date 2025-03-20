package killercreepr.cruxstatistics.core.statistic;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.cruxstatistics.api.statistic.CruxStatistic;
import killercreepr.cruxstatistics.api.statistic.CruxStatisticKeyedType;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

public class KeyedCruxStatisticType<T extends Keyed> implements CruxStatisticKeyedType<T> {
    protected final Key key;
    private final Map<T, CruxStatistic<T>> map = new IdentityHashMap<>();
    protected final Holder<KeyedRegistry<T>> registry;

    public KeyedCruxStatisticType(Key key, Holder<KeyedRegistry<T>> registry) {
        this.key = key;
        this.registry = registry;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof KeyedCruxStatisticType<?> that)) return false;
        return Objects.equals(key, that.key) && Objects.equals(map, that.map) && Objects.equals(registry, that.registry);
    }

    @Override
    public String toString() {
        return "KeyedCruxStatisticType{" +
            "key=" + key +
            ", map=" + map +
            ", registry=" + registry +
            '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, map, registry);
    }

    @Override
    public CruxStatistic<T> get(T value) {
        return map.computeIfAbsent(value, (type) -> CruxStatistic.statisticKeyed(this, value));
    }

    @Override
    public boolean contains(T value) {
        return map.containsKey(value);
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public @NotNull KeyedRegistry<T> getRegistry() {
        return registry.value();
    }
}
