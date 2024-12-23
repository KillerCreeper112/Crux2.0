package killercreepr.cruxstructures.api.structure.generation;

import killercreepr.cruxstructures.api.structure.Structure;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface StructureRequirement {
    boolean test(@NotNull Structure structure, @NotNull Chunk chunk, @NotNull Location location);

}
