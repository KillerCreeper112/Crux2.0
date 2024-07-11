package killercreepr.cruxstructures.structure.active;

import killercreepr.crux.data.BlockPos;
import killercreepr.cruxstructures.structure.stored.StoredStructure;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class SimpleActiveStructure implements ActiveStructure {
    protected final @NotNull StoredStructure stored;
    protected final @NotNull Block center;

    public SimpleActiveStructure(@NotNull StoredStructure stored, @NotNull Chunk chunk) {
        this.stored = stored;
        this.center = stored.getBlockPos().getBlock(chunk.getWorld());
    }

    @Override
    public @NotNull StoredStructure getData() {
        return stored;
    }

    @Override
    public @NotNull Block getCenter() {
        return center;
    }

    @Override
    public @NotNull BlockPos getBlockPos() {
        return stored.getBlockPos();
    }

    @Override
    public void tick() {}
}
