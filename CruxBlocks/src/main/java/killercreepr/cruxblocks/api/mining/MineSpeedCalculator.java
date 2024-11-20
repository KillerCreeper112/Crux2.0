package killercreepr.cruxblocks.api.mining;

import killercreepr.crux.api.loot.LootContext;
import org.jetbrains.annotations.NotNull;

public interface MineSpeedCalculator {
    float getMineSpeed(@NotNull LootContext ctx);
}
