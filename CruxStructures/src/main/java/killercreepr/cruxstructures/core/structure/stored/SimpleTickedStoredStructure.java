package killercreepr.cruxstructures.core.structure.stored;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.data.world.StoredChunk;
import killercreepr.cruxstructures.api.component.TickedStoredComponent;
import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.api.structure.TickedStoredStructure;
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
    public void started() {
        forEachAllOfType(TickedStoredComponent.class, TickedStoredComponent::storedStarted);
    }

    @Override
    public void tick() {
        forEachAllOfType(TickedStoredComponent.class, TickedStoredComponent::storedTick);
    }

    @Override
    public void stopped() {
        forEachAllOfType(TickedStoredComponent.class, TickedStoredComponent::storedStopped);
    }
}
