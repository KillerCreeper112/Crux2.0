package killercreepr.cruxstatistics.api.statistic;

import killercreepr.crux.api.data.Loadable;
import org.jetbrains.annotations.NotNull;

public interface CruxStatisticHolder extends Loadable {
    int getStatistic(@NotNull CruxStatisticType<?> type);
    int getStatistic(@NotNull CruxStatistic<?> statistic);
    <T> int getStatistic(@NotNull CruxStatisticType<T> type, T value);

    <T> void setStatistic(@NotNull CruxStatisticType<T> type, T value, int amount);
    void setStatistic(@NotNull CruxStatisticType<?> type, int amount);
    void setStatistic(@NotNull CruxStatistic<?> type, int amount);
    <T> void incrementStatistic(@NotNull CruxStatisticType<T> type, T value, int amount);
    void incrementStatistic(@NotNull CruxStatisticType<?> type, int amount);
    void incrementStatistic(@NotNull CruxStatistic<?> type, int amount);
    <T> void decrementStatistic(@NotNull CruxStatisticType<T> type, T value, int amount);
    void decrementStatistic(@NotNull CruxStatisticType<?> type, int amount);
    void decrementStatistic(@NotNull CruxStatistic<?> type, int amount);
}
