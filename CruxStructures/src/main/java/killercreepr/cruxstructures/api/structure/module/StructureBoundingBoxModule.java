package killercreepr.cruxstructures.api.structure.module;

import killercreepr.cruxstructures.api.structure.Structure;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StructureBoundingBoxModule extends StructureModule {
    @Override
    default void onPlaced(@NotNull Structure structure, @NotNull Location at, double rotation){}

    @Nullable BoundingBox editBoundingBox(@NotNull Structure structure, @NotNull Location at, double rotation, @NotNull BoundingBox box);
}
