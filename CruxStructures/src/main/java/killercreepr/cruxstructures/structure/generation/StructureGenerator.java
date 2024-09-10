package killercreepr.cruxstructures.structure.generation;

import killercreepr.cruxstructures.structure.GenerateResult;
import killercreepr.cruxstructures.structure.Structure;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface StructureGenerator {
    @NotNull
    GenerateResult
    generate(@NotNull Structure structure, @NotNull Chunk at);
    @NotNull GenerateResult generate(@NotNull Chunk at);

    @NotNull GenerateResult generate(@NotNull Location at);

    @NotNull GenerateResult generate(@NotNull Structure structure, @NotNull Location at);
}
