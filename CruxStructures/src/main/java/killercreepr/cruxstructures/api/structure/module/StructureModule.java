package killercreepr.cruxstructures.api.structure.module;

import killercreepr.cruxstructures.api.structure.Structure;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface StructureModule {
    void onPlaced(@NotNull Structure structure, @NotNull Location at, double rotation);
}
