package killercreepr.cruxstructures.core.structure.generation.requirement;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.api.structure.generation.StructureRequirement;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

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
    public CompletableFuture<Boolean> test(
      @NotNull Structure structure,
      @NotNull Chunk chunk,
      @NotNull Location location
    ) {
        return isValidNearby(
          location.getWorld(),
          range,
          location.getBlockX(),
          location.getBlockY(),
          location.getBlockZ()
        );
    }

    public abstract boolean isValidBlock(@NotNull Block block);

    public boolean isValidAmount(int amount) {
        if (amount < min) return false;
        return isValidMaxAmount(amount);
    }

    public boolean isValidMaxAmount(int amount) {
        return max == null || amount <= max;
    }

    public CompletableFuture<Boolean> isValidNearby(
      @NotNull World world,
      int range,
      int x,
      int y,
      int z
    ) {
        int yOff = yOffset == null ? 0 : yOffset.sample().intValue();
        return scanNearby(world, range, x, y, z, yOff, -range, -range, -range, 0);
    }

    private CompletableFuture<Boolean> scanNearby(
      @NotNull World world,
      int range,
      int originX,
      int originY,
      int originZ,
      int yOff,
      int xRange,
      int zRange,
      int yRange,
      int amount
    ) {
        if (xRange > range) {
            return CompletableFuture.completedFuture(isValidAmount(amount));
        }

        if (zRange > range) {
            return scanNearby(world, range, originX, originY, originZ, yOff, xRange + 1, -range, -range, amount);
        }

        if (yRange > range) {
            return scanNearby(world, range, originX, originY, originZ, yOff, xRange, zRange + 1, -range, amount);
        }

        if (xRange == 0 && yRange == 0 && zRange == 0) {
            return scanNearby(world, range, originX, originY, originZ, yOff, xRange, zRange, yRange + 1, amount);
        }

        int blockX = originX + xRange;
        int blockY = originY + yRange + yOff;
        int blockZ = originZ + zRange;

        int chunkX = blockX >> 4;
        int chunkZ = blockZ >> 4;

        return world.getChunkAtAsync(chunkX, chunkZ).thenCompose(chunk -> {
            Block block = world.getBlockAt(blockX, blockY, blockZ);

            int newAmount = amount;
            if (isValidBlock(block)) {
                newAmount++;
                if (!isValidMaxAmount(newAmount)) {
                    return CompletableFuture.completedFuture(false);
                }
            }

            return scanNearby(
              world, range, originX, originY, originZ, yOff,
              xRange, zRange, yRange + 1, newAmount
            );
        });
    }
}
