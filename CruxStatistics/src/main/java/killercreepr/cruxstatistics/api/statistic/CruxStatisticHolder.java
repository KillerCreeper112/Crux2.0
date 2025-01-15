package killercreepr.cruxstatistics.api.statistic;

import killercreepr.crux.api.data.Loadable;
import org.jetbrains.annotations.NotNull;

public interface CruxStatisticHolder extends Loadable {
    int getStatistic(@NotNull CruxStatisticType<?> type);
    int getStatistic(@NotNull CruxStatistic<?> statistic);
    <T> int getStatistic(@NotNull CruxStatisticType<T> type, T value);
}
