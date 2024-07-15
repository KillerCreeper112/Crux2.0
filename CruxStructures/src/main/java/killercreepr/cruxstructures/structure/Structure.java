package killercreepr.cruxstructures.structure;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.world.block.BlockState;
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

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

public interface Structure extends Keyed {
    @NotNull
    default StructurePlaceEvent place(@NotNull Location at){
        return place(at, new Random().nextInt(4) * 90);
    }

    @NotNull
    StructurePlaceEvent place(@NotNull Location at, double rotation);
    @NotNull
    BoundingBox boundingBox();
    @NotNull BlockPos originPos();

    default boolean isPersistent(){ return false; }
    default @Nullable StoredStructure buildStored(@NotNull Location center, double rotation){
        return new SimpleStoredStructure(this, StoredChunk.from(center), BlockPos.from(center), rotation);
    }

    @NotNull Collection<BlockPos> getBlocks(double rotation);
}
