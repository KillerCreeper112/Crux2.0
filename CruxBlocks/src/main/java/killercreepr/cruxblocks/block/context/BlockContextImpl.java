package killercreepr.cruxblocks.block.context;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.data.Holder;
import killercreepr.cruxblocks.user.Miner;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class BlockContextImpl implements BlockContext{
    protected final @NotNull Block block;
    protected final @Nullable Miner miner;
    protected final @NotNull DataExchange info;
    public BlockContextImpl(@NotNull Block block, @Nullable Miner miner, @NotNull DataExchange info) {
        this.block = block;
        this.miner = miner;
        this.info = info.append(Map.of(
            "block", Holder.direct(block),
            "miner", Holder.direct(miner)
        ));
    }

    @Override
    public @NotNull Block getBlock() {
        return block;
    }

    @Override
    public @Nullable Miner getMiner() {
        return miner;
    }

    @Override
    public @NotNull DataExchange info() {
        return info;
    }

    @Override
    public @NotNull BlockContext withBlock(@NotNull Block block) {
        return new BlockContextImpl(block, miner, info);
    }

    @Override
    public @NotNull BlockContext withMiner(@Nullable Miner miner) {
        return new BlockContextImpl(block, miner, info);
    }

    @Override
    public @NotNull BlockContext withInfo(@NotNull DataExchange info) {
        return new BlockContextImpl(block, miner, info);
    }
}
