package killercreepr.cruxstatistics.api.statistic;

import killercreepr.crux.api.data.CruxKeyed;
import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.cruxstatistics.core.statistic.KeyedCruxStatisticType;
import killercreepr.cruxstatistics.core.statistic.SimpleCruxStatisticType;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;

public interface CruxStatisticType<T> extends CruxKeyed {
    static CruxStatisticType<?> statisticType(Key key){
        return new SimpleCruxStatisticType<>(key);
    }

    static <T extends Keyed> CruxStatisticKeyedType<T> statisticTypeKeyed(Key key, KeyedRegistry<T> registry){
        return statisticTypeKeyed(key, Holder.direct(registry));
    }

    static <T extends Keyed> CruxStatisticKeyedType<T> statisticTypeKeyed(Key key, Holder<KeyedRegistry<T>> registry){
        return new KeyedCruxStatisticType<>(key, registry);
    }

    default CruxStatistic<T> getUnchecked(Object value){
        return get((T) value);
    }

    CruxStatistic<T> get(T value);
    boolean contains(T value);
}
