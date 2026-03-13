package killercreepr.cruxstructures.api.structure.generation;

import killercreepr.cruxstructures.api.structure.Structure;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface StructureRequirement {
    CompletableFuture<Boolean> test(@NotNull Structure structure, @NotNull Chunk chunk, @NotNull Location location);

}
