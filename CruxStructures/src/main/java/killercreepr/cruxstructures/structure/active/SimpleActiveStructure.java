package killercreepr.cruxstructures.structure.active;

import killercreepr.cruxstructures.structure.Structure;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

public class SimpleActiveStructure implements ActiveStructure {
    protected final @NotNull Structure structure;
    protected final @NotNull Block center;
    public SimpleActiveStructure(@NotNull Structure structure, @NotNull Block center) {
        this.structure = structure;
        this.center = center;
    }

    @Override
    public @NotNull Structure parent() {
        return structure;
    }

    @Override
    public @NotNull Block center() {
        return center;
    }

    @Override
    public @NotNull Block getMinPoint() {
        BoundingBox box = structure.boundingBox();
        return center.getRelative(
            (int) box.getMinX(), (int) box.getMinY(), (int) box.getMinZ()
        );
    }

    @Override
    public @NotNull Block getMaxPoint() {
        BoundingBox box = structure.boundingBox();
        return center.getRelative(
            (int) box.getMaxX(), (int) box.getMaxY(), (int) box.getMaxZ()
        );
    }
}
