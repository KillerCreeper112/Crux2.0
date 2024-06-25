package killercreepr.cruxblocks.block.context;

import killercreepr.cruxblocks.user.Miner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceBlockContextImpl extends BlockContextImpl implements PlaceBlockContext{
    protected final @NotNull BlockFace blockFace;
    public PlaceBlockContextImpl(@NotNull Block block, @Nullable Miner miner, @NotNull BlockFace blockFace) {
        super(block, miner);
        this.blockFace = blockFace;
    }

    @Override
    public @NotNull BlockFace getBlockFace() {
        return blockFace;
    }
}
