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
        this.boundingBox = calculateBoundingBox(center, structure);
        /*Vector min = structure.boundingBox().getMin();
        Vector max = structure.boundingBox().getMax();
        this.boundingBox = new BoundingBox(
            center.x() + min.getX(),
            center.y() + min.getY(),
            center.z() + min.getZ(),

            center.x() + max.getX(),
            center.y() + max.getY(),
            center.z() + max.getZ()
        );*/
    }


    public static BoundingBox adjustBoundingBoxToBlockRegion(BoundingBox originalBox) {
        // Get the current min and max coordinates of the original BoundingBox
        double minX = originalBox.getMinX();
        double minY = originalBox.getMinY();
        double minZ = originalBox.getMinZ();
        double maxX = originalBox.getMaxX();
        double maxY = originalBox.getMaxY();
        double maxZ = originalBox.getMaxZ();

        // Adjust the coordinates to ensure they align with block boundaries
        int adjustedMinX = (int) Math.floor(minX); // Round down to nearest integer
        int adjustedMinY = (int) Math.floor(minY);
        int adjustedMinZ = (int) Math.floor(minZ);
        int adjustedMaxX = (int) Math.ceil(maxX); // Round up to nearest integer
        int adjustedMaxY = (int) Math.ceil(maxY);
        int adjustedMaxZ = (int) Math.ceil(maxZ);

        // Create a new BoundingBox with adjusted coordinates
        return new BoundingBox(adjustedMinX, adjustedMinY, adjustedMinZ, adjustedMaxX, adjustedMaxY, adjustedMaxZ);
    }

    public @NotNull BoundingBox calculateBoundingBox(@NotNull BlockPos center, @NotNull Structure structure){
        BlockPos origin = structure.originPos();

        int offsetX = center.x() - origin.x();
        int offsetY = center.y() - origin.y();
        int offsetZ = center.z() - origin.z();

        BoundingBox box = structure.boundingBox().clone().shift(offsetX, offsetY, offsetZ);
        //if(true) return box.expand(1);
        //if(true) return adjustBoundingBoxToBlockRegion(box);
        return new BoundingBox(
            box.getMinX(), box.getMinY(), box.getMinZ(),
            box.getMaxX()+1, box.getMaxY()+1, box.getMaxZ()+1
        ).shift(1, 0, -1);

        /*Vector min = structureBox.getMin();
        Vector max = structureBox.getMax();

        // Calculate half-widths in each dimension
        double halfWidthX = (max.getX() - min.getX()) / 2.0;
        double halfWidthY = (max.getY() - min.getY()) / 2.0;
        double halfWidthZ = (max.getZ() - min.getZ()) / 2.0;

        // Calculate the center of the structure's bounding box
        double centerX = center.x();//center.x() + (min.getX() + max.getX()) / 2.0;
        double centerY = center.y();//center.y() + (min.getY() + max.getY()) / 2.0;
        double centerZ = center.z();//center.z() + (min.getZ() + max.getZ()) / 2.0;

        // Calculate the bounding box around the center point
        return new BoundingBox(
            centerX - halfWidthX,
            centerY - halfWidthY,
            centerZ - halfWidthZ,
            centerX + halfWidthX,
            centerY + halfWidthY,
            centerZ + halfWidthZ
        );*/
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
