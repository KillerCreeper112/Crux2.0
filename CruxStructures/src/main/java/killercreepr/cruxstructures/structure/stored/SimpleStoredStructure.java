package killercreepr.cruxstructures.structure.stored;

import killercreepr.crux.data.BlockPos;
import killercreepr.crux.data.StoredChunk;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.active.ActiveStructure;
import killercreepr.cruxstructures.structure.active.SimpleActiveStructure;
import net.kyori.adventure.key.Key;
import org.bukkit.Chunk;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleStoredStructure implements StoredStructure{
    protected final @NotNull Key structureKey;
    protected final @NotNull StoredChunk chunk;
    protected final @NotNull BlockPos center;
    protected final @NotNull BoundingBox boundingBox;
    public SimpleStoredStructure(@NotNull Structure structure, @NotNull StoredChunk chunk, @NotNull BlockPos center) {
        this.structureKey = structure.key();
        this.chunk = chunk;
        this.center = center;
        Vector min = structure.boundingBox().getMin();
        Vector max = structure.boundingBox().getMax();
        this.boundingBox = new BoundingBox(
            center.x() + min.getX(),
            center.y() + min.getY(),
            center.z() + min.getZ(),

            center.x() + max.getX(),
            center.y() + max.getY(),
            center.z() + max.getZ()
        );
    }

    public SimpleStoredStructure(@NotNull Key structureKey, @NotNull StoredChunk chunk, @NotNull BlockPos center, @NotNull BoundingBox boundingBox) {
        this.structureKey = structureKey;
        this.chunk = chunk;
        this.center = center;
        this.boundingBox = boundingBox;
    }

    @Override
    public @NotNull Key getStructureKey() {
        return structureKey;
    }

    @Override
    public @NotNull StoredChunk getChunk() {
        return chunk;
    }

    @Override
    public @NotNull BlockPos getBlockPos() {
        return center;
    }

    @Override
    public @NotNull BoundingBox getBoundingBox() {
        return boundingBox;
    }

    @Override
    public @Nullable ActiveStructure buildActive(@NotNull Chunk chunk) {
        return new SimpleActiveStructure(this, chunk);
    }

}
