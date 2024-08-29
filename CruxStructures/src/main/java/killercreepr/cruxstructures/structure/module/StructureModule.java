package killercreepr.cruxstructures.structure.module;

import killercreepr.cruxstructures.structure.Structure;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface StructureModule {
    void onPlaced(@NotNull Structure structure, @NotNull Location at, double rotation);
}
