package killercreepr.cruxblocks.core.mining.user;

import killercreepr.cruxblocks.api.mining.user.Miner;
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

    @Override
    public Object getHandle() {
        return getBlock();
    }
}
