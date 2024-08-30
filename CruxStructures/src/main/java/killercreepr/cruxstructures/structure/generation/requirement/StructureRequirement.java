package killercreepr.cruxstructures.structure.generation.requirement;

import killercreepr.cruxstructures.structure.Structure;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface StructureRequirement {
    boolean test(@NotNull Structure structure, @NotNull Chunk chunk, @NotNull Location location);

}
