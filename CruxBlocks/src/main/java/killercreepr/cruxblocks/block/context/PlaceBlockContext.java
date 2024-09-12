package killercreepr.cruxblocks.block.context;

import killercreepr.crux.data.DataExchange;
import killercreepr.cruxblocks.user.Miner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PlaceBlockContext extends BlockContext{
    static PlaceBlockContext context(@NotNull Block block, @Nullable Miner miner, @NotNull DataExchange info, @NotNull BlockFace blockFace){
        return new PlaceBlockContextImpl(block, miner, info, blockFace);
    }
    static PlaceBlockContext context(@NotNull Block block, @Nullable Miner miner, @NotNull BlockFace blockFace){
        return context(block, miner, DataExchange.EMPTY, blockFace);
    }

    @NotNull
    BlockFace getBlockFace();
    @Contract(pure = true)
    @NotNull PlaceBlockContext withBlockFace(@NotNull BlockFace blockFace);

    @Override
    @NotNull PlaceBlockContext withBlock(@NotNull Block block);

    @Override
    @NotNull PlaceBlockContext withMiner(@Nullable Miner miner);
}
