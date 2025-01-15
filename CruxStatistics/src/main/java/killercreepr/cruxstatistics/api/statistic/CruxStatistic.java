package killercreepr.cruxstatistics.api.statistic;

import killercreepr.cruxstatistics.core.statistic.KeyedStatistic;
import killercreepr.cruxstatistics.core.statistic.SimpleStatistic;
import net.kyori.adventure.key.Keyed;

public interface CruxStatistic<T> {
    static <T extends Keyed> CruxStatistic<T> statisticKeyed(CruxStatisticType<T> type, T value){
        return new KeyedStatistic<>(type, value);
    }
    static CruxStatistic<?> statistic(CruxStatisticType<?> type){
        return new SimpleStatistic(type);
    }
    static <T extends Keyed> CruxStatistic<T> statisticKeyedUnchecked(CruxStatisticType<T> type, Object value){
        return new KeyedStatistic<>(type, (T) value);
    }

    CruxStatisticType<T> getType();
    T getValue();
    String getName();
}
