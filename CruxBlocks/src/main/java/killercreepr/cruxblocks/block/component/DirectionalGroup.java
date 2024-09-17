package killercreepr.cruxblocks.block.component;

import killercreepr.cruxblocks.block.CruxBlock;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DirectionalGroup {
    @Nullable
    CruxBlock getBlock(@NotNull BlockFace direction);
    boolean isOrientable();
}
