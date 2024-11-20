package killercreepr.cruxblocks.mining;

import killercreepr.crux.api.loot.LootContext;
import org.jetbrains.annotations.NotNull;

public interface MineSpeedCalculator {
    float getMineSpeed(@NotNull LootContext ctx);
}
