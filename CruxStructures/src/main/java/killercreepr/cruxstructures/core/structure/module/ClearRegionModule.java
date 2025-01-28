package killercreepr.cruxstructures.core.structure.module;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.api.valueproviders.vector.NumberVector;
import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.api.structure.module.StructureModule;
import killercreepr.cruxstructures.core.util.CruxStructureUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class ClearRegionModule implements StructureModule {
    protected final @NotNull NumberVector pos1;
    protected final @NotNull NumberVector pos2;

    public ClearRegionModule(@NotNull NumberVector pos1, @NotNull NumberVector pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    @Override
    public void onPlaced(@NotNull Structure structure, @NotNull Location at, double rotation) {
        int pos1X = pos1.x().value().intValue();
        int pos1Y = pos1.y().value().intValue();
        int pos1Z = pos1.z().value().intValue();

        int pos2X = pos2.x().value().intValue();
        int pos2Y = pos2.y().value().intValue();
        int pos2Z = pos2.z().value().intValue();

        CruxPosition placedAt = CruxPosition.location(at);
        CruxPosition center = CruxStructureUtil.fromStructureToWorldPos(structure, placedAt, structure.originPos());

        CruxPosition pos1 = center.add(pos1X, pos1Y, pos1Z).rotateAroundY(center, rotation);
        CruxPosition pos2 = center.add(pos2X, pos2Y, pos2Z).rotateAroundY(center, rotation);

        try(EditSession session = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(at.getWorld()))){
            CuboidRegion region = new CuboidRegion(
                BlockVector3.at(pos1.x(), pos1.y(), pos1.z()),
                BlockVector3.at(pos2.x(), pos2.y(), pos2.z())
            );
            session.setBlocks((Region) region, BukkitAdapter.adapt(Material.AIR.createBlockData()));
        }
    }
}
