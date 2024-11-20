package killercreepr.cruxblocks.core.block.context;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.cruxblocks.api.block.context.MineBlockContext;
import killercreepr.cruxblocks.api.mining.user.Miner;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MineBlockContextImpl extends BlockContextImpl implements MineBlockContext {
    public MineBlockContextImpl(@NotNull Block block, @Nullable Miner miner, @NotNull DataExchange info) {
        super(block, miner, info);
    }
}
