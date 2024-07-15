package killercreepr.cruxstructures.structure.stored;

import killercreepr.crux.data.BlockPos;
import killercreepr.crux.data.StoredChunk;
import killercreepr.crux.data.world.BlockPosed;
import killercreepr.cruxstructures.registries.StructureRegistries;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.active.ActiveStructure;
import killercreepr.cruxstructures.structure.impl.CfgStoredBlocksStructure;
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

    double getRotation();

    default boolean shouldPersist(){ return getParent().isPersistent(); }

    @NotNull
    BoundingBox getBoundingBox();

    @Nullable
    ActiveStructure buildActive(@NotNull Chunk chunk);

    default @NotNull BlockPos fromWorldToStructurePos(@NotNull BlockPos worldPos){
        BlockPos placedCenter = getBlockPos();
        BlockPos structureOrigin = getParent().originPos();

        BlockPos difference = placedCenter.subtract(structureOrigin);
        return worldPos.subtract(difference);
    }
}
