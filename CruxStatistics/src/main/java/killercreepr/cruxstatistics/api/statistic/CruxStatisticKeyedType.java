package killercreepr.cruxstatistics.api.statistic;

import killercreepr.crux.api.registry.KeyedRegistry;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

public interface CruxStatisticKeyedType<T extends Keyed> extends CruxStatisticType<T> {
    @NotNull KeyedRegistry<T> getRegistry();
}
