package killercreepr.cruxstats.core.registries;

import killercreepr.crux.registry.KeyedRegistry;
import killercreepr.crux.registry.SimpleKeyedRegistry;
import killercreepr.cruxstats.api.stat.CruxStat;

public class CruxStatRegistries {
    public static final KeyedRegistry<CruxStat> STAT = new SimpleKeyedRegistry<>();
}
