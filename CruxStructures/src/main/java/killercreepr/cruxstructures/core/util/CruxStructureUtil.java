package killercreepr.cruxstructures.core.util;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.util.CruxedBoundingBox;
import killercreepr.cruxstructures.api.structure.Structure;
import org.bukkit.util.BoundingBox;
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

    public static @NotNull BoundingBox calculateBoundingBox(@NotNull CruxPosition center, @NotNull Structure structure, double rotation) {
        CruxPosition origin = structure.originPos();
        return CruxedBoundingBox.wrap(structure.boundingBox())
            .moveTo(origin, center)
            .rotateY(
                rotation, center.x() + .5, center.y() + .5, center.z() + .5
            )
            .box()
            ;
    }
}
