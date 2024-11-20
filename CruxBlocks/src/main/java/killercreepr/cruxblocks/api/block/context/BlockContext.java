package killercreepr.cruxblocks.api.block.context;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.cruxblocks.api.mining.user.Miner;
import killercreepr.cruxblocks.core.block.context.BlockContextImpl;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BlockContext {
    static BlockContext context(@NotNull Block block, @Nullable Miner miner, @NotNull DataExchange info){
        return new BlockContextImpl(block, miner, info);
    }
    static BlockContext context(@NotNull Block block, @Nullable Miner miner){
        return context(block, miner, DataExchange.empty());
    }
    @NotNull
    Block getBlock();
    @Nullable
    Miner getMiner();
    @NotNull
    DataExchange info();

    @Contract(pure = true)
    @NotNull BlockContext withBlock(@NotNull Block block);
    @Contract(pure = true)
    @NotNull BlockContext withMiner(@Nullable Miner miner);
    @Contract(pure = true)
    @NotNull BlockContext withInfo(@NotNull DataExchange info);
}
