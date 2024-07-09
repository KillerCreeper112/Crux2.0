package killercreepr.cruxstructures.structure;

import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StructureGenerator {
    @NotNull GenerateResult
    generate(@NotNull Structure structure, @NotNull Chunk at);
}
