package killercreepr.cruxstructures.api.component;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.data.world.StoredChunk;
import killercreepr.cruxstructures.api.structure.StoredStructure;
import killercreepr.cruxstructures.api.structure.Structure;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StructureEditorComponent {
    /*default @Nullable StoredStructure onCreated(@NotNull Location center, double rotation, @NotNull Structure structure, @Nullable StoredStructure stored){
        return onCreated(StoredChunk.from(center), CruxPosition.block(center), rotation, structure, stored);
    }*/
    @Nullable StoredStructure onCreated(@NotNull StoredChunk chunk, @NotNull CruxPosition center, double rotation, @NotNull Structure structure, @Nullable StoredStructure stored);
}