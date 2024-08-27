package killercreepr.cruxblocks.block.context;

import killercreepr.cruxblocks.user.Miner;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BlockContext {
    static BlockContext context(@NotNull Block block, @Nullable Miner miner){
        return new BlockContextImpl(block, miner);
    }
    @NotNull
    Block getBlock();
    @Nullable
    Miner getMiner();

    @Contract(pure = true)
    @NotNull BlockContext withBlock(@NotNull Block block);
    @Contract(pure = true)
    @NotNull BlockContext withMiner(@Nullable Miner miner);
}
