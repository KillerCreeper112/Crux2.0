package killercreepr.cruxstructures.core.structure.generation.requirement;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StructureReplaceableNearbyRequirement extends AbstractStructureNearbyRequirement {

    public StructureReplaceableNearbyRequirement(int range, int min, @Nullable Integer max, @Nullable NumberProvider yOffset) {
        super(range, min, max, yOffset);
    }

    @Override
    public boolean isValidBlock(@NotNull Block block) {
        return block.isEmpty() || block.isReplaceable();
    }
}
