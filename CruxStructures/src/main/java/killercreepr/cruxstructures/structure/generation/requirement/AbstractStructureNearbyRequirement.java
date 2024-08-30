package killercreepr.cruxstructures.structure.generation.requirement;

import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxstructures.structure.Structure;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractStructureNearbyRequirement implements StructureRequirement {
    protected final int range;
    protected final int min;
    protected final @Nullable Integer max;
    protected final @Nullable NumberProvider yOffset;
    public AbstractStructureNearbyRequirement(int range, int min, @Nullable Integer max, @Nullable NumberProvider yOffset) {
        this.range = range;
        this.min = min;
        this.max = max;
        this.yOffset = yOffset;
    }

    @Override
    public boolean test(@NotNull Structure structure, @NotNull Chunk chunk, @NotNull Location location) {
        return isValidNearby(location.getWorld(), range, location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public abstract boolean isValidBlock(@NotNull Block block);
    public boolean isValidAmount(int amount){
        if(amount < min) return false;
        return max == null || amount <= max;
    }

    public boolean isValidNearby(@NotNull World world, int range, int x, int y, int z){
        int amount = 0;
        for(int xRange = -range; xRange <= range; xRange++){
            for(int zRange = -range; zRange <= range; zRange++){
                for(int yRange = -range; yRange <= range; yRange++){
                    if(xRange == 0 && yRange == 0 && zRange == 0) continue;
                    Block block = world.getBlockAt(x + xRange, y + yRange + (yOffset == null ? 0 : yOffset.sample().intValue()), z + zRange);
                    if(isValidBlock(block)){
                        amount++;
                        if(!isValidAmount(amount)) return false;
                    }
                }
            }
        }
        return true;
    }
}
