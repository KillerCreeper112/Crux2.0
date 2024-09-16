package killercreepr.cruxblocks.config.block;

import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CfgBlock extends CruxBlock {
    @Nullable
    CfgBlockGroup getCfgGroup();

    default @NotNull ActiveCruxBlock createActive(@NotNull Block block) {
        return new CfgActiveBlock(block, this);
    }

}
