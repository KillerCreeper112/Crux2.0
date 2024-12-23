package killercreepr.cruxstructures.core.structure.stored;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.data.world.StoredChunk;
import killercreepr.cruxstructures.api.structure.Structure;
import net.kyori.adventure.key.Key;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CfgStoredStructure extends SimpleStoredStructure {
    protected final @Nullable BoundingBox innerBox;
    public CfgStoredStructure(@NotNull Structure structure, @NotNull StoredChunk chunk,
                              @NotNull CruxPosition center, double rotation,
                              @Nullable BoundingBox innerBox) {
        super(structure, chunk, center, rotation);
        this.innerBox = innerBox;
    }

    public CfgStoredStructure(@NotNull Key structureKey, @NotNull StoredChunk chunk,
                              @NotNull CruxPosition center, @NotNull BoundingBox boundingBox, double rotation,
                              @Nullable BoundingBox innerBox) {
        super(structureKey, chunk, center, boundingBox, rotation);
        this.innerBox = innerBox;
    }

    public @Nullable BoundingBox getInnerBox() {
        return innerBox;
    }
}
