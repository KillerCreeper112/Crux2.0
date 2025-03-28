package killercreepr.cruxstructures.core.structure.generation.center;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.api.structure.generation.StructureCenter;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RandomAnywhereCenter implements StructureCenter {
    protected final NumberProvider yRange;

    public RandomAnywhereCenter(NumberProvider yRange) {
        this.yRange = yRange;
    }


    @Override
    public @Nullable Location scan(@NotNull Structure structure, @NotNull Chunk chunk) {
        int x = (chunk.getX() << 4) + CruxMath.random(0, 15);
        int z = (chunk.getZ() << 4) + CruxMath.random(0, 15);

        Block b = chunk.getWorld().getBlockAt(x, yRange.value().intValue(), z);
        return new Location(chunk.getWorld(), x, b.getY(), z);
    }
}
