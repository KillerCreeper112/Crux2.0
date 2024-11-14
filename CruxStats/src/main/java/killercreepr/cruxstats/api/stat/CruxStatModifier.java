package killercreepr.cruxstats.api.stat;

import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

public interface CruxStatModifier extends Keyed {
    double getAmount();
    @NotNull CruxStat.Operation getOperation();
}
