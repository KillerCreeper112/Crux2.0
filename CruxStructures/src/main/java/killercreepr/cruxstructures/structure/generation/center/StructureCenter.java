package killercreepr.cruxstructures.structure.generation.center;

import killercreepr.cruxstructures.structure.Structure;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StructureCenter {
    @Nullable
    Location scan(@NotNull Structure structure, @NotNull Chunk chunk);
}
