package killercreepr.cruxblocks.core.block;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.active.ActiveCruxBlock;
import net.kyori.adventure.key.Key;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class CruxCruxedBlock implements CruxedBlock {
    protected final @NotNull ActiveCruxBlock block;
    public CruxCruxedBlock(@NotNull ActiveCruxBlock block) {
        this.block = block;
    }

    @Override
    public @NotNull Key getType() {
        CruxBlock crux = block.getCruxBlock();
        if(crux.getGroup() != null) return crux.getGroup().key();
        return crux.key();
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

    @Override
    public @NotNull Block getBlock() {
        return block.getBlock();
    }
}
