package killercreepr.cruxstructures.structure;

import killercreepr.cruxstructures.structure.stored.StoredStructure;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

public interface InnerBoxedStructure extends StoredStructure {
    @NotNull
    BoundingBox getInnerBox();
}
