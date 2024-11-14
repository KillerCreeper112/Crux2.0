package killercreepr.cruxstats.core.stat;

import killercreepr.cruxstats.api.stat.CruxStat;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class SimpleStat implements CruxStat {
    protected final @NotNull Key key;
    public SimpleStat(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
