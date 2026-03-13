package killercreepr.cruxstructures.api.structure.generation;

import killercreepr.cruxstructures.api.structure.Structure;
import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;

public interface StructureChunkRequirement {
    boolean test(@NotNull Chunk chunk);
}
