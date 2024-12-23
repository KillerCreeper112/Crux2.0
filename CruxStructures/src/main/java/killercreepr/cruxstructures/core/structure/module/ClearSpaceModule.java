package killercreepr.cruxstructures.core.structure.module;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.api.structure.module.StructureModule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class ClearSpaceModule implements StructureModule {
    protected final @NotNull NumberProvider radius;
    protected final @NotNull NumberProvider height;

    public ClearSpaceModule(@NotNull NumberProvider radius, @NotNull NumberProvider height) {
        this.radius = radius;
        this.height = height;
    }

    @Override
    public void onPlaced(@NotNull Structure structure, @NotNull Location at, double rotation) {
        int radius = this.radius.value().intValue();
        int height = this.height.value().intValue();
        try(EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(at.getWorld()))){
            CuboidRegion region = new CuboidRegion(
                BlockVector3.at(at.getBlockX() - radius, at.getBlockY(), at.getBlockZ() - radius),
                BlockVector3.at(at.getBlockX() + radius, at.getBlockY() + height, at.getBlockZ() + radius)
            );
            session.setBlocks((Region) region, BukkitAdapter.adapt(Material.AIR.createBlockData()));
        }
    }
}
