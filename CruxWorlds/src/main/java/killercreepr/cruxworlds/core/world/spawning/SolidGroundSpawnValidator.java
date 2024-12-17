package killercreepr.cruxworlds.core.world.spawning;

import killercreepr.cruxworlds.api.world.entity.SpawnContext;
import killercreepr.cruxworlds.api.world.spawning.SpawnValidator;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public class SolidGroundSpawnValidator implements SpawnValidator {
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
