package killercreepr.cruxstructures.core.structure.component;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.data.world.StoredChunk;
import killercreepr.cruxstructures.api.component.StructureEditorComponent;
import killercreepr.cruxstructures.api.structure.StoredStructure;
import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.core.structure.stored.SimpleStoredStructure;
import killercreepr.cruxstructures.core.structure.stored.SimpleTickedStoredStructure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StructureTickedStoredComponent implements StructureEditorComponent {
    @Override
    public @Nullable StoredStructure onCreated(@NotNull StoredChunk chunk, @NotNull CruxPosition center, double rotation, @NotNull Structure structure, @Nullable StoredStructure stored) {
        if(stored == null){
            return new SimpleStoredStructure(structure, chunk, center, rotation);
        }
        return new SimpleTickedStoredStructure(
            stored.getStructureKey(), stored.getChunk(), stored.getPosition(), stored.getBoundingBox(), stored.getRotation()
        );
    }
}
