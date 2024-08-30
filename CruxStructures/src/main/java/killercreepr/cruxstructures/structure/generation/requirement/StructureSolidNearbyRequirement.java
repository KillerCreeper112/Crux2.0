package killercreepr.cruxstructures.structure.generation.requirement;

import killercreepr.crux.valueproviders.number.NumberProvider;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StructureSolidNearbyRequirement extends AbstractStructureNearbyRequirement {

    public StructureSolidNearbyRequirement(int range, int min, @Nullable Integer max, @Nullable NumberProvider yOffset) {
        super(range, min, max, yOffset);
    }

    @Override
    public boolean isValidBlock(@NotNull Block block) {
        return block.isSolid();
    }
}
