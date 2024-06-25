package killercreepr.cruxblocks.block.context;

import killercreepr.cruxblocks.user.Miner;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockContextImpl implements BlockContext{
    protected final @NotNull Block block;
    protected final @Nullable Miner miner;
    public BlockContextImpl(@NotNull Block block, @Nullable Miner miner) {
        this.block = block;
        this.miner = miner;
    }

    @Override
    public @NotNull Block getBlock() {
        return block;
    }

    @Override
    public @Nullable Miner getMiner() {
        return miner;
    }
}
