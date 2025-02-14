package killercreepr.cruxblocks.api.event;

import org.bukkit.ExplosionResult;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.List;

public class CustomBlockExplodeEvent extends CustomExplodeEvent{
    protected final Block block;

    public CustomBlockExplodeEvent(Location location, List<Block> blocks, List<Block> wrappedBlocks, ExplosionResult result, float yield, Block block) {
        super(location, blocks, wrappedBlocks, result, yield);
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }
}
