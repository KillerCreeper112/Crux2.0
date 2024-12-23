package killercreepr.cruxstructures.api.structure;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.data.world.StoredChunk;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxstructures.api.event.StructurePlaceEvent;
import killercreepr.cruxstructures.core.structure.stored.SimpleStoredStructure;
import net.kyori.adventure.key.Keyed;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Predicate;

public interface Structure extends Keyed {
    @NotNull
    default StructurePlaceEvent place(@NotNull Location at){
        return place(at, CruxMath.random().nextInt(4) * 90);
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

    default @NotNull Collection<CruxPosition> getBlocks(double rotation){
        return getBlocks(rotation, null);
    }
    @NotNull Collection<CruxPosition> getBlocks(double rotation, @Nullable Predicate<CruxPosition> filter);
}
