package killercreepr.cruxblocks.config.block;

import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlockImpl;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class CfgActiveBlock extends ActiveCruxBlockImpl {
    public CfgActiveBlock(@NotNull Block block, @NotNull CruxBlock cruxBlock) {
        super(block, cruxBlock);
    }
}
