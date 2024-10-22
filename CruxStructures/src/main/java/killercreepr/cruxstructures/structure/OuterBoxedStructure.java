package killercreepr.cruxstructures.structure;

import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

public interface OuterBoxedStructure {
    @NotNull
    BoundingBox getOuterBox();
}
