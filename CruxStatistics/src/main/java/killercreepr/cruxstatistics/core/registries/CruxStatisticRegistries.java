package killercreepr.cruxstatistics.core.registries;

import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.cruxstatistics.api.statistic.CruxStatisticType;

public class CruxStatisticRegistries {
    public static final KeyedRegistry<CruxStatisticType<?>> STATISTIC_TYPE = KeyedRegistry.keyedRegistry();
}
