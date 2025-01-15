package killercreepr.cruxstatistics.core.statistic;

import killercreepr.cruxstatistics.api.statistic.CruxStatistic;
import killercreepr.cruxstatistics.api.statistic.CruxStatisticType;
import net.kyori.adventure.key.Keyed;

public class KeyedStatistic<T extends Keyed> implements CruxStatistic<T> {
    protected final CruxStatisticType<T> type;
    protected final T value;

    public KeyedStatistic(CruxStatisticType<T> type, T value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public CruxStatisticType<T> getType() {
        return type;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public String getName() {
        return type.key().asString() + "/" + value.key().asString();
    }
}
