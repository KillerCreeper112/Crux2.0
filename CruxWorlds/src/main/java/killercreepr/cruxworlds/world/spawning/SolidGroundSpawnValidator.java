package killercreepr.cruxworlds.world.spawning;

import killercreepr.cruxworlds.world.entity.SpawnContext;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public class SolidGroundSpawnValidator implements SpawnValidator{
    @Override
    public boolean canSpawn(@NotNull SpawnContext ctx) {
        Block b = ctx.getBlock();
        if(!b.isPassable() || b.isLiquid()) return false;
        Block check = b.getRelative(BlockFace.UP);
        if(!check.isPassable() || check.isLiquid()) return false;

        check = b.getRelative(BlockFace.DOWN);
        if(!check.isSolid() || check.isLiquid()) return false;
        return true;
    }
}
