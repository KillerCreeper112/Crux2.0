package killercreepr.cruxstructures.api.structure;

import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.api.math.PositionPosed;
import killercreepr.crux.core.data.world.StoredChunk;
import killercreepr.cruxstructures.core.registries.StructureRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.Chunk;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface StoredStructure extends PositionPosed, DataComponentHandler {
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

    default @NotNull CruxPosition fromWorldToStructurePos(@NotNull CruxPosition worldPos){
        CruxPosition placedCenter = getPosition();
        CruxPosition structureOrigin = getParent().originPos();

        CruxPosition difference = placedCenter.subtract(structureOrigin);
        return worldPos.subtract(difference);
    }
}
