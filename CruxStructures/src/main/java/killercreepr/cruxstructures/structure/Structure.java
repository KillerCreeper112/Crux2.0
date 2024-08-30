package killercreepr.cruxstructures.structure;

import killercreepr.crux.data.StoredChunk;
import killercreepr.crux.data.world.CruxPosition;
import killercreepr.crux.util.CruxMath;
import killercreepr.cruxstructures.event.StructurePlaceEvent;
import killercreepr.cruxstructures.structure.stored.SimpleStoredStructure;
import killercreepr.cruxstructures.structure.stored.StoredStructure;
import net.kyori.adventure.key.Keyed;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface Structure extends Keyed {
    @NotNull
    default StructurePlaceEvent place(@NotNull Location at){
        return place(at, CruxMath.RANDOM.nextInt(4) * 90);
    }

    @NotNull
    StructurePlaceEvent place(@NotNull Location at, double rotation);
    @NotNull
    BoundingBox boundingBox();
    @NotNull
    CruxPosition originPos();

    default boolean isPersistent(){ return false; }
    default @Nullable StoredStructure buildStored(@NotNull Location center, double rotation){
        return new SimpleStoredStructure(this, StoredChunk.from(center), CruxPosition.block(center), rotation);
    }

    @NotNull Collection<CruxPosition> getBlocks(double rotation);
}
