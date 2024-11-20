package killercreepr.cruxstats.api.stat;

import killercreepr.crux.api.data.Loadable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CruxStatHolder extends Loadable {
    @Nullable CruxStatInstance getStat(@NotNull CruxStat stat);
    @NotNull CruxStatInstance getOrLoadStat(@NotNull CruxStat stat);
    default double getStatValue(@NotNull CruxStat stat){
        CruxStatInstance instance = getStat(stat);
        if(instance == null) return 0D;
        return instance.getValue();
    }

    default double getOrLoadStatValue(@NotNull CruxStat stat){
        CruxStatInstance instance = getOrLoadStat(stat);
        return instance.getValue();
    }
}
