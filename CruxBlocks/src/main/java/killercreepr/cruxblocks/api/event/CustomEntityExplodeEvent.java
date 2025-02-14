package killercreepr.cruxblocks.api.event;

import org.bukkit.ExplosionResult;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.List;

public class CustomEntityExplodeEvent extends CustomExplodeEvent{
    protected final Entity entity;

    public CustomEntityExplodeEvent(Location location, List<Block> blocks, List<Block> wrappedBlocks, ExplosionResult result, float yield, Entity entity) {
        super(location, blocks, wrappedBlocks, result, yield);
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
