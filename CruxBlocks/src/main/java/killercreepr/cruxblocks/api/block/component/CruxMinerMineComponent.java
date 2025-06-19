package killercreepr.cruxblocks.api.block.component;

import killercreepr.cruxblocks.api.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.api.mining.user.Miner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CruxMinerMineComponent extends CruxBlockComponent {
    @Nullable Float onMinerMine(@Nullable Miner miner, @NotNull ActiveCruxBlock block);
}
