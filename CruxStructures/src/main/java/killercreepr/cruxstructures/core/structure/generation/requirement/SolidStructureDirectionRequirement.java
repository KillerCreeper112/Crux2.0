package killercreepr.cruxstructures.core.structure.generation.requirement;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SolidStructureDirectionRequirement extends AbstractStructureDirectionTestRequirement{
    public SolidStructureDirectionRequirement(@NotNull Map<BlockFace, NumberProvider> directions, @NotNull NumberProvider minDirectionAmount) {
        super(directions, minDirectionAmount);
    }

    @Override
    public boolean isValidBlock(@NotNull Block block) {
        return block.isSolid() && !block.isLiquid();
    }
}
