package killercreepr.cruxstats.core.stat;

import killercreepr.cruxstats.api.stat.CruxStat;
import killercreepr.cruxstats.api.stat.CruxStatHolder;
import killercreepr.cruxstats.api.stat.CruxStatInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class DataStatHolder implements CruxStatHolder {
    protected final @NotNull Map<CruxStat, CruxStatInstance> stats = new HashMap<>();
    @Override
    public @Nullable CruxStatInstance getStat(@NotNull CruxStat stat) {
        return stats.get(stat);
    }

    @Override
    public @NotNull CruxStatInstance getOrLoadStat(@NotNull CruxStat stat) {
        CruxStatInstance instance = getStat(stat);
        if(instance != null) return instance;
        instance = stat.createNewInstance();
        if(instance == null) throw new UnsupportedOperationException(stat.key() + " cannot create new stat instances!");
        stats.put(instance.getStat(), instance);
        return instance;
    }

    @Override
    public void save() {

    }

    @Override
    public void load() {

    }
}
