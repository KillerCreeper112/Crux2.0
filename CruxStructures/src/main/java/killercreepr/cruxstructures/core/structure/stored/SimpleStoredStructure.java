package killercreepr.cruxstructures.core.structure.stored;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.data.world.StoredChunk;
import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.api.structure.ActiveStructure;
import killercreepr.cruxstructures.core.structure.active.SimpleActiveStructure;
import killercreepr.cruxstructures.api.structure.StoredStructure;
import killercreepr.cruxstructures.core.util.CruxStructureUtil;
import net.kyori.adventure.key.Key;
import org.bukkit.Chunk;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleStoredStructure implements StoredStructure {
    protected final @NotNull Key structureKey;
    protected final @NotNull StoredChunk chunk;
    protected final @NotNull CruxPosition center;
    protected final @NotNull BoundingBox boundingBox;
    protected final double rotation;
    public SimpleStoredStructure(@NotNull Structure structure, @NotNull StoredChunk chunk, @NotNull CruxPosition center, double rotation) {
        this.structureKey = structure.key();
        this.chunk = chunk;
        this.center = center;
        this.rotation = rotation;
        this.boundingBox = calculateBoundingBox(center, structure);
    }

    public SimpleStoredStructure(@NotNull Key structureKey, @NotNull StoredChunk chunk, @NotNull CruxPosition center,
                                 @NotNull BoundingBox boundingBox,
                                 double rotation) {
        this.structureKey = structureKey;
        this.chunk = chunk;
        this.center = center;
        this.boundingBox = boundingBox;
        this.rotation = rotation;
    }

    public @NotNull BoundingBox calculateBoundingBox(@NotNull CruxPosition center, @NotNull Structure structure){
        return CruxStructureUtil.calculateBoundingBox(center, structure, rotation);

        /*CruxPosition origin = structure.originPos();

        int offsetX = center.blockX() - origin.blockX();
        int offsetY = center.blockY() - origin.blockY();
        int offsetZ = center.blockZ() - origin.blockZ();

        BoundingBox box = structure.boundingBox().clone();

        box = box.shift(offsetX, offsetY, offsetZ);
        box = new BoundingBox(
            box.getMinX(), box.getMinY(), box.getMinZ(),
            box.getMaxX()+1, box.getMaxY()+1, box.getMaxZ()+1
        );

        return new CruxedBoundingBox(box).rotateY(rotation, center.x()+.5, center.y()+.5, center.z()+.5).box();*/
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
    public @NotNull CruxPosition getPosition() {
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
