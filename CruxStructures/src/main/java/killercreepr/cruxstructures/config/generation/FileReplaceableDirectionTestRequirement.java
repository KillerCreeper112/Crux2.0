package killercreepr.cruxstructures.config.generation;

import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxstructures.structure.generation.requirement.ReplaceableStructureDirectionRequirement;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class FileReplaceableDirectionTestRequirement extends AbstractFileDirectionTestRequirement<ReplaceableStructureDirectionRequirement> {
    @Override
    public @Nullable ReplaceableStructureDirectionRequirement parse(@NotNull FileContext<?> ctx, @NotNull FileObject o, @NotNull Map<BlockFace, NumberProvider> directions, @NotNull NumberProvider minDirectionAmount) {
        return new ReplaceableStructureDirectionRequirement(directions, minDirectionAmount);
    }
}
