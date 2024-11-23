package killercreepr.cruxstructures.structure.generation.center;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxstructures.structure.Structure;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RandomSurfaceDeterminedCenter extends RandomSurfaceCenter {
    public RandomSurfaceDeterminedCenter(int maxAttempts, @Nullable NumberProvider yLimit) {
        super(maxAttempts, yLimit);
    }

    @Override
    public @Nullable Location scan(@NotNull Structure structure, @NotNull Chunk chunk) {
        Location l = super.scan(structure, chunk);
        if(l != null) return l;
        int x = (chunk.getX() << 4) + CruxMath.random(0, 15);
        int z = (chunk.getZ() << 4) + CruxMath.random(0, 15);

        return chunk.getWorld().getHighestBlockAt(x, z).getLocation();
    }

    @Override
    public int randomY(@NotNull World world, int x, int z) {
        return world.getHighestBlockAt(x, z).getY();
    }
}
