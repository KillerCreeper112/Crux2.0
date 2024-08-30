package killercreepr.cruxstructures.structure.generation.center;

import killercreepr.crux.util.CruxMath;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxstructures.structure.Structure;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RandomSurfaceCenter implements StructureCenter {
    protected final int maxAttempts;
    protected final @Nullable NumberProvider yLimit;

    public RandomSurfaceCenter(int maxAttempts, @Nullable NumberProvider yLimit) {
        this.maxAttempts = maxAttempts;
        this.yLimit = yLimit;
    }

    @Override
    public @Nullable Location scan(@NotNull Structure structure, @NotNull Chunk chunk) {
        World world = chunk.getWorld();

        int chunkX = (chunk.getX() << 4) + 8;
        int chunkZ = (chunk.getZ() << 4) + 8;

        int attempts = 0;
        while(attempts < maxAttempts){
            attempts++;
            int x = chunkX + CruxMath.random(0, 15);
            int z = chunkZ + CruxMath.random(0, 15);
            int y = randomY(world);
            Block block = world.getBlockAt(x, y, z);
            if(!block.getType().isSolid()) continue;
            return block.getLocation();
        }
        return null;
    }

    public int randomY(@NotNull World world){
        if(yLimit == null) return CruxMath.random(world.getMinHeight()+1, world.getMaxHeight()-1);
        return yLimit.sample().intValue();
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public @Nullable NumberProvider getyLimit() {
        return yLimit;
    }
}
