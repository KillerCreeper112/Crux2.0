package killercreepr.cruxstats.core.stat;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxstats.api.stat.CruxStat;
import killercreepr.cruxstats.api.stat.CruxStatInstance;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleStat implements CruxStat {
    protected final @NotNull Key key;
    protected final @NotNull NumberProvider baseValue;
    public SimpleStat(@NotNull Key key, @NotNull NumberProvider baseValue) {
        this.key = key;
        this.baseValue = baseValue;
    }

    @Override
    public @NotNull NumberProvider baseValue() {
        return baseValue;
    }

    @Override
    public @Nullable CruxStatInstance createNewInstance() {
        return new SimpleCruxStatInstance(this);
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
