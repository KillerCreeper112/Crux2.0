package killercreepr.cruxblocks.api.block.context;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.cruxblocks.api.mining.user.Miner;
import killercreepr.cruxblocks.core.block.context.MineBlockContextImpl;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MineBlockContext extends BlockContext{
    static MineBlockContext context(@NotNull Block block, @Nullable Miner miner, @NotNull DataExchange info){
        return new MineBlockContextImpl(block, miner, info);
    }
    static MineBlockContext context(@NotNull Block block, @Nullable Miner miner){
        return context(block, miner, DataExchange.empty());
    }
}
