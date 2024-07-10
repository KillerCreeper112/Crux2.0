package killercreepr.cruxstructures.structure;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StructureCenter {
    @Nullable
    Location scan(@NotNull Structure structure, @NotNull Chunk chunk);
    @NotNull String type();
}
