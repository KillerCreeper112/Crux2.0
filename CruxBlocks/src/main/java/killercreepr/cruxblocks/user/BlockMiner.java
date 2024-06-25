package killercreepr.cruxblocks.user;

import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class BlockMiner implements Miner {
    protected final @NotNull Block block;
    public BlockMiner(@NotNull Block block) {
        this.block = block;
    }

    public @NotNull Block getBlock() {
        return block;
    }
}
