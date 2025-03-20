package killercreepr.cruxstatistics.core.statistic;

import killercreepr.cruxstatistics.api.statistic.CruxStatistic;
import killercreepr.cruxstatistics.api.statistic.CruxStatisticType;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SimpleStatistic that)) return false;
        return Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type);
    }

    @Override
    public String toString() {
        return "SimpleStatistic{" +
            "type=" + type +
            '}';
    }
}
