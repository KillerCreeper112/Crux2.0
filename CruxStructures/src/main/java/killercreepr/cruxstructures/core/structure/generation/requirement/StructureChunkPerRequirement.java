package killercreepr.cruxstructures.core.structure.generation.requirement;

import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.api.structure.generation.StructureChunkRequirement;
import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class StructureChunkPerRequirement implements StructureChunkRequirement {
    protected final double per;
    protected final double chunk;
    protected final double cache;
    public StructureChunkPerRequirement(double per, double chunk) {
        this.per = per;
        this.chunk = chunk;
        this.cache = per / chunk;
    }

    @Override
    public boolean test(@NotNull Structure structure, @NotNull Chunk chunk) {
        return ThreadLocalRandom.current().nextDouble() <= cache;
    }
}
