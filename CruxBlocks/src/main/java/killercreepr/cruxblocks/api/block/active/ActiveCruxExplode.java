package killercreepr.cruxblocks.api.block.active;

import killercreepr.cruxblocks.api.event.CustomExplodeEvent;
import killercreepr.cruxblocks.api.mining.user.Miner;
import org.jetbrains.annotations.NotNull;

public interface ActiveCruxExplode {
    @NotNull Result exploded(CustomExplodeEvent event, Miner miner);

    enum Result {
        BREAK_WITH_DROP,
        BREAK_WITHOUT_DROP,
        DENY,
        DEFAULT
    }
}
