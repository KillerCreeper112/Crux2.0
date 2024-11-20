package killercreepr.cruxstructures.util;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.cruxstructures.structure.Structure;
import org.jetbrains.annotations.NotNull;

public class CruxStructureUtil {

    public static @NotNull CruxPosition fromWorldToStructurePos(@NotNull Structure structure,
                                                                @NotNull CruxPosition placedCenter,
                                                                @NotNull CruxPosition worldPos){
        return fromWorldToStructurePos(
            structure.originPos(),
            placedCenter, worldPos
        );
    }

    public static @NotNull CruxPosition fromWorldToStructurePos(@NotNull CruxPosition structureOrigin,
                                                                @NotNull CruxPosition placedCenter,
                                                                @NotNull CruxPosition worldPos){
        CruxPosition difference = placedCenter.subtract(structureOrigin);
        return worldPos.subtract(difference);
    }

    public static @NotNull CruxPosition fromStructureToWorldPos(@NotNull Structure structure,
                                                                @NotNull CruxPosition placedCenter,
                                                                @NotNull CruxPosition structurePos) {
        return fromStructureToWorldPos(
            structure.originPos(),
            placedCenter, structurePos
        );
    }

    public static @NotNull CruxPosition fromStructureToWorldPos(@NotNull CruxPosition structureOrigin,
                                                                @NotNull CruxPosition placedCenter,
                                                                @NotNull CruxPosition structurePos) {
        CruxPosition difference = placedCenter.subtract(structureOrigin);
        return structurePos.add(difference);
    }
}
