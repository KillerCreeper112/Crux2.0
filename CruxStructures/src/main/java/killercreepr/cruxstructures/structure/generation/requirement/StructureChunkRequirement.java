package killercreepr.cruxstructures.structure.generation.requirement;

import killercreepr.cruxstructures.structure.Structure;
import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;

public interface StructureChunkRequirement {
    boolean test(@NotNull Structure structure, @NotNull Chunk chunk);
}
