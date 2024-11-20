package killercreepr.cruxstructures.structure.active;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.cruxstructures.structure.stored.StoredStructure;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class SimpleActiveStructure implements ActiveStructure {
    protected final @NotNull StoredStructure stored;
    protected final @NotNull Block center;

    public SimpleActiveStructure(@NotNull StoredStructure stored, @NotNull Chunk chunk) {
        this.stored = stored;
        this.center = stored.getPosition().getBlock(chunk.getWorld());
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
    public @NotNull CruxPosition getPosition() {
        return stored.getPosition();
    }

    @Override
    public void tick() {}
}
