package killercreepr.cruxstructures.api.structure.generation;

import killercreepr.cruxstructures.api.structure.Structure;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StructureCenter {
    @Nullable
    Location scan(@NotNull Structure structure, @NotNull Chunk chunk);
}
