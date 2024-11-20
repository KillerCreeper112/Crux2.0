package killercreepr.cruxstats.core.registries;

import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.crux.core.registry.SimpleKeyedRegistry;
import killercreepr.cruxstats.api.stat.CruxStat;

public class CruxStatRegistries {
    public static final KeyedRegistry<CruxStat> STAT = new SimpleKeyedRegistry<>();
}
