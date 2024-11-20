package killercreepr.cruxblocks.block.context;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.cruxblocks.user.Miner;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MineBlockContextImpl extends BlockContextImpl implements MineBlockContext{
    public MineBlockContextImpl(@NotNull Block block, @Nullable Miner miner, @NotNull DataExchange info) {
        super(block, miner, info);
    }
}
