package killercreepr.cruxstructures.api.structure;

import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

public interface InnerBoxedStructure extends StoredStructure {
    @NotNull
    BoundingBox getInnerBox();
}
