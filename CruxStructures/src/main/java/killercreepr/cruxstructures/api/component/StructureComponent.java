package killercreepr.cruxstructures.api.component;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.data.world.StoredChunk;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxstructures.api.structure.StoredStructure;
import killercreepr.cruxstructures.api.structure.Structure;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface StructureComponent {
    /*default void onCreated(@NotNull Location center, double rotation, @NotNull StoredStructure stored){
        onCreated(StoredChunk.from(center), CruxPosition.block(center), rotation, stored);
    }*/
    default void onCreated(@NotNull StoredChunk chunk, @NotNull CruxPosition center, double rotation, @NotNull StoredStructure stored){
    }
    default void onPlaced(@NotNull Structure structure, @NotNull Location at, double rotation){

    }

    default void onFileLoad(@NotNull FileContext<?> context, @NotNull FileObject o, @NotNull StoredStructure structure){
    }

    default void onStructureHook(@NotNull Structure structure){

    }
}