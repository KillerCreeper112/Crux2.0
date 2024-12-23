package killercreepr.cruxstructures.api.structure;

import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.Nullable;

public interface InnerBoxedStructure extends StoredStructure {
    @Nullable
    BoundingBox getInnerBox();
}
