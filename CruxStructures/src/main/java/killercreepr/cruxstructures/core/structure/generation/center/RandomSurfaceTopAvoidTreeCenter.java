package killercreepr.cruxstructures.core.structure.generation.center;

import com.destroystokyo.paper.MaterialSetTag;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxstructures.api.structure.generation.StructureCenter;
import killercreepr.cruxstructures.api.structure.Structure;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RandomSurfaceTopAvoidTreeCenter implements StructureCenter {
    protected final int maxFindValidGroundAttempts;
    public RandomSurfaceTopAvoidTreeCenter(int maxFindValidGroundAttempts) {
        this.maxFindValidGroundAttempts = maxFindValidGroundAttempts;
    }

    @Override
    public @Nullable Location scan(@NotNull Structure structure, @NotNull Chunk chunk) {
        int x = (chunk.getX() << 4) + CruxMath.random(0, 15);
        int z = (chunk.getZ() << 4) + CruxMath.random(0, 15);
        Block b = chunk.getWorld().getHighestBlockAt(x, z);
        if(!b.isSolid()) return null;

        if(isLeaves(b)){
            b = findValidGround(b);
            if(b==null) return null;
        }

        return new Location(chunk.getWorld(), x, b.getY(), z);
    }

    public Block findValidGround(@NotNull Block leaves){
        int attempts = 0;
        Block current = leaves;
        while(attempts < maxFindValidGroundAttempts){
            attempts++;
            current = current.getRelative(BlockFace.DOWN);
            if(isValidSpace(current)) return current;
        }
        return null;
    }

    public boolean isValidSpace(@NotNull Block block){
        if(isLeaves(block)) return false;
        if(!block.isEmpty() && !block.isReplaceable()) return false;
        Block ground = block.getRelative(BlockFace.DOWN);
        return !isLeaves(ground) && ground.isSolid();
    }

    public boolean isLeaves(@NotNull Block block){
        return MaterialSetTag.LEAVES.isTagged(block.getType());
    }
}
