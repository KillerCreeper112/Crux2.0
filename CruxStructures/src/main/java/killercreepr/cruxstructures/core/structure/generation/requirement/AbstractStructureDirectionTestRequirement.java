package killercreepr.cruxstructures.core.structure.generation.requirement;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.api.structure.generation.StructureRequirement;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractStructureDirectionTestRequirement implements StructureRequirement {
    protected final @NotNull Map<BlockFace, NumberProvider> directions;
    protected final @NotNull NumberProvider minDirectionAmount;

    public AbstractStructureDirectionTestRequirement(@NotNull Map<BlockFace, NumberProvider> directions, @NotNull NumberProvider minDirectionAmount) {
        this.directions = directions;
        this.minDirectionAmount = minDirectionAmount;
    }

    @Override
    public CompletableFuture<Boolean> test(
      @NotNull Structure structure,
      @NotNull Chunk chunk,
      @NotNull Location location
    ) {
        Block block = location.getBlock();
        int min = minDirectionAmount.value().intValue();

        var entries = new ArrayList<>(directions.entrySet());
        return testDirectionsSequentially(block, entries, 0, 0, min);
    }

    private CompletableFuture<Boolean> testDirectionsSequentially(
      @NotNull Block block,
      @NotNull List<Map.Entry<BlockFace, NumberProvider>> entries,
      int index,
      int passed,
      int min
    ) {
        if (passed >= min) {
            return CompletableFuture.completedFuture(true);
        }

        if (index >= entries.size()) {
            return CompletableFuture.completedFuture(false);
        }

        Map.Entry<BlockFace, NumberProvider> entry = entries.get(index);
        int amount = entry.getValue().value().intValue();

        return test(block, entry.getKey(), amount).thenCompose(success -> {
            int newPassed = success ? passed + 1 : passed;
            return testDirectionsSequentially(block, entries, index + 1, newPassed, min);
        });
    }

    public CompletableFuture<Boolean> test(@NotNull Block b, @NotNull BlockFace face, int amount) {
        return testStep(b, face, amount);
    }

    private CompletableFuture<Boolean> testStep(@NotNull Block b, @NotNull BlockFace face, int amount) {
        if (amount <= 0) {
            return CompletableFuture.completedFuture(true);
        }

        var world = b.getWorld();
        int chunkX = b.getX() >> 4;
        int chunkZ = b.getZ() >> 4;

        return world.getChunkAtAsync(chunkX, chunkZ).thenCompose(chunk -> {
            Block next = b.getRelative(face);

            if (!isValidBlock(next)) {
                return CompletableFuture.completedFuture(false);
            }

            return testStep(next, face, amount - 1);
        });
    }

    public abstract boolean isValidBlock(@NotNull Block block);
}
