package killercreepr.cruxstructures.structure.stored;

import com.sk89q.worldedit.session.ClipboardHolder;
import killercreepr.crux.data.BlockPos;
import killercreepr.crux.data.StoredChunk;
import killercreepr.crux.util.CruxedBoundingBox;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.active.ActiveStructure;
import killercreepr.cruxstructures.structure.active.SimpleActiveStructure;
import net.kyori.adventure.key.Key;
import org.bukkit.Chunk;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class SimpleStoredStructure implements StoredStructure{
    protected final @NotNull Key structureKey;
    protected final @NotNull StoredChunk chunk;
    protected final @NotNull BlockPos center;
    protected final @NotNull BoundingBox boundingBox;
    protected final double rotation;
    public SimpleStoredStructure(@NotNull Structure structure, @NotNull StoredChunk chunk, @NotNull BlockPos center, double rotation) {
        this.structureKey = structure.key();
        this.chunk = chunk;
        this.center = center;
        this.rotation = rotation;
        this.boundingBox = calculateBoundingBox(center, structure);
    }

    public SimpleStoredStructure(@NotNull Key structureKey, @NotNull StoredChunk chunk, @NotNull BlockPos center, @NotNull BoundingBox boundingBox, double rotation) {
        this.structureKey = structureKey;
        this.chunk = chunk;
        this.center = center;
        this.boundingBox = boundingBox;
        this.rotation = rotation;
    }

    public @NotNull BoundingBox calculateBoundingBox(@NotNull BlockPos center, @NotNull Structure structure){
        BlockPos origin = structure.originPos();

        int offsetX = center.x() - origin.x();
        int offsetY = center.y() - origin.y();
        int offsetZ = center.z() - origin.z();

        BoundingBox box = structure.boundingBox().clone();

        box = box.shift(offsetX, offsetY, offsetZ);
        box = new BoundingBox(
            box.getMinX(), box.getMinY(), box.getMinZ(),
            box.getMaxX()+1, box.getMaxY()+1, box.getMaxZ()+1
        );

        return new CruxedBoundingBox(box).rotateY(rotation, center.x()+.5, center.y()+.5, center.z()+.5);
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
    public double getRotation() {
        return rotation;
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
