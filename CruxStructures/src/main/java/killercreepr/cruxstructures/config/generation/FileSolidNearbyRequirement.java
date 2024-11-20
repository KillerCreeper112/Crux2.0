package killercreepr.cruxstructures.config.generation;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxstructures.structure.generation.requirement.StructureSolidNearbyRequirement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileSolidNearbyRequirement extends AbstractFileNearbyRequirement<StructureSolidNearbyRequirement> {
    @Override
    public @Nullable StructureSolidNearbyRequirement parse(@NotNull FileContext<?> ctx, @NotNull FileObject o, Integer range, Integer min, Integer max, NumberProvider yOffset) {
        return new StructureSolidNearbyRequirement(range, min, max, yOffset);
    }
}
