package killercreepr.cruxstructures.api.component;

import killercreepr.crux.api.data.world.StoredChunk;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.cruxstructures.api.structure.StoredStructure;
import killercreepr.cruxstructures.api.structure.Structure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StructureEditorComponent {
    /*default @Nullable StoredStructure onCreated(@NotNull Location center, double rotation, @NotNull Structure structure, @Nullable StoredStructure stored){
        return onCreated(StoredChunk.from(center), CruxPosition.block(center), rotation, structure, stored);
    }*/
    @Nullable StoredStructure onCreated(@NotNull StoredChunk chunk, @NotNull CruxPosition center, double rotation, @NotNull Structure structure, @Nullable StoredStructure stored);
}