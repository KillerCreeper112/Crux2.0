package killercreepr.cruxblocks.block;

import killercreepr.crux.block.CruxedBlock;
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
}
