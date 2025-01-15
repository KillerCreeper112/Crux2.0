package killercreepr.cruxstatistics.core.statistic;

import killercreepr.cruxstatistics.api.statistic.CruxStatistic;
import killercreepr.cruxstatistics.api.statistic.CruxStatisticType;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class SimpleCruxStatisticType<T> implements CruxStatisticType<T>, CruxStatistic<T> {
    protected final Key key;

    public SimpleCruxStatisticType(Key key) {
        this.key = key;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public CruxStatisticType<T> getType() {
        return this;
    }

    @Override
    public T getValue() {
        return null;
    }

    @Override
    public String getName() {
        return key.asString();
    }

    @Override
    public CruxStatistic<T> get(T value) {
        return this;
    }

    @Override
    public boolean contains(T value) {
        return false;
    }
}
