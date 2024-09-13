package killercreepr.cruxblocks.block;

import killercreepr.crux.block.CruxedBlock;
import killercreepr.crux.data.world.CruxPosition;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class CruxCruxedBlock implements CruxedBlock {
    protected final @NotNull ActiveCruxBlock block;
    public CruxCruxedBlock(@NotNull ActiveCruxBlock block) {
        this.block = block;
    }

    @Override
    public @NotNull Key getType() {
        return block.getCruxBlock().key();
    }

    @Override
    public @NotNull CruxPosition getPosition() {
        return CruxPosition.block(block.getBlock());
    }

    @Override
    public int getX() {
        return block.getBlock().getX();
    }

    @Override
    public int getY() {
        return block.getBlock().getY();
    }

    @Override
    public int getZ() {
        return block.getBlock().getZ();
    }
}
