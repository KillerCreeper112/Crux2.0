package killercreepr.cruxstatistics.core.statistic;

import killercreepr.cruxstatistics.api.statistic.CruxStatistic;
import killercreepr.cruxstatistics.api.statistic.CruxStatisticType;

public class SimpleStatistic implements CruxStatistic {
    protected final CruxStatisticType<?> type;
    public SimpleStatistic(CruxStatisticType<?> type) {
        this.type = type;
    }

    @Override
    public CruxStatisticType<?> getType() {
        return type;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public String getName() {
        return type.key().asString();
    }
}
