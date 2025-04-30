package killercreepr.cruxblocks.core.block;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.cruxblocks.api.block.CruxBlock;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;

public class CruxCruxedBlockState implements CruxedBlock {
    protected final @NotNull CruxBlock block;
    protected final @NotNull BlockState state;
    public CruxCruxedBlockState(@NotNull CruxBlock block, @NotNull BlockState state) {
        this.block = block;
        this.state = state;
    }

    @Override
    public @NotNull Key getType() {
        if(block.getGroup() != null) return block.getGroup().key();
        return block.key();
    }

    @Override
    public @NotNull CruxPosition getPosition() {
        return CruxPosition.block(state.getBlock());
    }

    @Override
    public int getX() {
        return state.getX();
    }

    @Override
    public int getY() {
        return state.getY();
    }

    @Override
    public int getZ() {
        return state.getZ();
    }

    @Override
    public @NotNull Block getBlock() {
        return state.getBlock();
    }
}
