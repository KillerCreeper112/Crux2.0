package killercreepr.cruxstructures.core.config.generation;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxstructures.core.structure.generation.requirement.StructureReplaceableNearbyRequirement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileReplaceableNearbyRequirement extends AbstractFileNearbyRequirement<StructureReplaceableNearbyRequirement> {
    @Override
    public @Nullable StructureReplaceableNearbyRequirement parse(@NotNull FileContext<?> ctx, @NotNull FileObject o, Integer range, Integer min, Integer max, NumberProvider yOffset) {
        return new StructureReplaceableNearbyRequirement(range, min, max, yOffset);
    }
}
