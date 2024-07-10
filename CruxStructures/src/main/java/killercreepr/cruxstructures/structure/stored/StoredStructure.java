package killercreepr.cruxstructures.structure.stored;

import killercreepr.crux.data.StoredChunk;
import killercreepr.crux.data.world.BlockPosed;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.active.ActiveStructure;
import org.bukkit.Chunk;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StoredStructure extends BlockPosed {
    @NotNull
    Structure getParent();
    @NotNull
    StoredChunk getChunk();

    default boolean shouldPersist(){ return getParent().isPersistent(); }

    @NotNull
    BoundingBox getBoundingBox();

    @Nullable
    ActiveStructure buildActive(@NotNull Chunk chunk);
}
