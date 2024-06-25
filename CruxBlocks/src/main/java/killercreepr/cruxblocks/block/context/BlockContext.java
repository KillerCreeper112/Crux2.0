package killercreepr.cruxblocks.block.context;

import killercreepr.cruxblocks.user.Miner;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BlockContext {
    @NotNull
    Block getBlock();
    @Nullable
    Miner getMiner();
}
