package killercreepr.cruxstructures.structure;

import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;

public interface StructureGenerator {
    void generate(@NotNull Structure structure, @NotNull Chunk at);
}
