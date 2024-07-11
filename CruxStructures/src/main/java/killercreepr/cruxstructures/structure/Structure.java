package killercreepr.cruxstructures.structure;

import killercreepr.crux.data.BlockPos;
import killercreepr.crux.data.StoredChunk;
import killercreepr.cruxstructures.event.StructurePlaceEvent;
import killercreepr.cruxstructures.structure.stored.SimpleStoredStructure;
import killercreepr.cruxstructures.structure.stored.StoredStructure;
import net.kyori.adventure.key.Keyed;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Structure extends Keyed {
    @NotNull
    StructurePlaceEvent place(@NotNull Location at);
    @NotNull
    BoundingBox boundingBox();
    @NotNull BlockPos originPos();

    default boolean isPersistent(){ return false; }
    default @Nullable StoredStructure buildStored(@NotNull Location center){
        return new SimpleStoredStructure(this, StoredChunk.from(center), BlockPos.from(center));
    }
}
