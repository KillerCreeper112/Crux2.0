package killercreepr.cruxstructures.structure.stored;

import killercreepr.crux.data.StoredChunk;
import killercreepr.crux.data.world.BlockPosed;
import killercreepr.cruxstructures.registries.StructureRegistries;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.active.ActiveStructure;
import net.kyori.adventure.key.Key;
import org.bukkit.Chunk;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface StoredStructure extends BlockPosed {
    @NotNull
    Key getStructureKey();
    default @NotNull Structure getParent(){
        return Objects.requireNonNull(
            StructureRegistries.STRUCTURES.get(getStructureKey())
        );
    }
    @NotNull
    StoredChunk getChunk();

    default boolean shouldPersist(){ return getParent().isPersistent(); }

    @NotNull
    BoundingBox getBoundingBox();

    @Nullable
    ActiveStructure buildActive(@NotNull Chunk chunk);
}
