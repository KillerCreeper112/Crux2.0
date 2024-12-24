package killercreepr.cruxstructures.core.structure.active;

import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.crux.api.data.tick.ManagedTicked;
import killercreepr.crux.api.data.tick.Ticked;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.cruxstructures.api.structure.ActiveStructure;
import killercreepr.cruxstructures.api.structure.StoredStructure;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class SimpleActiveStructure extends DataComponentHandler.Simple implements ActiveStructure {
    protected final @NotNull StoredStructure stored;
    protected final @NotNull Block center;

    public SimpleActiveStructure(@NotNull StoredStructure stored, @NotNull Chunk chunk) {
        this.stored = stored;
        this.center = stored.getPosition().getBlock(chunk.getWorld());
    }

    @Override
    public void started() {
        forEachAllOfType(ManagedTicked.class, ManagedTicked::started);
    }

    @Override
    public void tick() {
        forEachAllOfType(Ticked.class, Ticked::tick);
    }

    @Override
    public void stopped() {
        forEachAllOfType(ManagedTicked.class, ManagedTicked::stopped);
    }

    @Override
    public @NotNull StoredStructure getData() {
        return stored;
    }

    @Override
    public @NotNull Block getCenter() {
        return center;
    }

    @Override
    public @NotNull CruxPosition getPosition() {
        return stored.getPosition();
    }
}
