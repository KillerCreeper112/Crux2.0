package killercreepr.cruxstructures.core.structure.generation.center;

import killercreepr.cruxstructures.api.structure.generation.StructureCenter;
import killercreepr.cruxstructures.api.structure.Structure;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SurfaceTopCenter implements StructureCenter {
    @Override
    public @Nullable Location scan(@NotNull Structure structure, @NotNull Chunk chunk) {
        int x = (chunk.getX() << 4) + 8;
        int z = (chunk.getZ() << 4) + 8;
        Block b = chunk.getWorld().getHighestBlockAt(x, z);
        if(!b.isSolid()) return null;
        return new Location(chunk.getWorld(), x, b.getY(), z);
    }
}
