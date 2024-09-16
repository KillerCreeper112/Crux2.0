package killercreepr.cruxblocks.config.block;

import killercreepr.cruxblocks.block.active.ActiveCruxBlockImpl;
import killercreepr.cruxblocks.user.Miner;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CfgActiveBlock extends ActiveCruxBlockImpl {
    protected final @NotNull SimpleCfgBlock cfgBlock;
    public CfgActiveBlock(@NotNull Block block, @NotNull SimpleCfgBlock cruxBlock) {
        super(block, cruxBlock);
        this.cfgBlock = cruxBlock;
    }

    @Override
    public boolean canHarvest(@Nullable Miner miner) {
        CfgBlockGroup group = cfgBlock.getCfgGroup();
        if(group != null){
            if(!group.isRequireCorrectToolForHarvest()) return true;
        }
        return super.canHarvest(miner);
    }
}
