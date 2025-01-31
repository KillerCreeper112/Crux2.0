package killercreepr.cruxstructures.core.structure.stored;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.data.world.StoredChunk;
import killercreepr.cruxstructures.api.component.TickedStoredComponent;
import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.api.structure.TickedStoredStructure;
import killercreepr.cruxstructures.api.world.module.StructureWorldModule;
import net.kyori.adventure.key.Key;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

public class SimpleTickedStoredStructure extends SimpleStoredStructure implements TickedStoredStructure {
    public SimpleTickedStoredStructure(@NotNull Structure structure, @NotNull StoredChunk chunk, @NotNull CruxPosition center, double rotation) {
        super(structure, chunk, center, rotation);
    }

    public SimpleTickedStoredStructure(@NotNull Key structureKey, @NotNull StoredChunk chunk, @NotNull CruxPosition center, @NotNull BoundingBox boundingBox, double rotation) {
        super(structureKey, chunk, center, boundingBox, rotation);
    }

    @Override
    public void started(@NotNull StructureWorldModule module) {
        forEachAllOfType(TickedStoredComponent.class, s -> s.storedStarted(module));
    }

    @Override
    public void tick(@NotNull StructureWorldModule module) {
        forEachAllOfType(TickedStoredComponent.class, s -> s.storedTick(module));
    }

    @Override
    public void stopped(@NotNull StructureWorldModule module) {
        forEachAllOfType(TickedStoredComponent.class, s -> s.storedStopped(module));
    }

    /*@Override
    public void tick(@NotNull StructureWorldModule module) {

    }*/
}
