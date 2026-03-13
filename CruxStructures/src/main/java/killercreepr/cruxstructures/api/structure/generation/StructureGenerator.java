package killercreepr.cruxstructures.api.structure.generation;

import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.api.structure.generation.result.GenerateResult;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public interface StructureGenerator {
    boolean canPlace(@NotNull Chunk at);

    @Nullable Structure generateStructure(@NotNull Chunk at);

    @NotNull
    CompletableFuture<GenerateResult> generate(@NotNull Structure structure, @NotNull Chunk at);

    @NotNull CompletableFuture<GenerateResult> generate(@NotNull Structure structure, @NotNull Location at);
}
