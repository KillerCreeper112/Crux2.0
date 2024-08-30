package killercreepr.cruxstructures.structure.generation.requirement;

import killercreepr.cruxstructures.structure.Structure;
import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class StructureChunkPerThousandRequirement implements StructureChunkRequirement {
    protected final int perThousandChunks;
    public StructureChunkPerThousandRequirement(int perThousandChunks) {
        this.perThousandChunks = perThousandChunks;
    }

    @Override
    public boolean test(@NotNull Structure structure, @NotNull Chunk chunk) {
        return ThreadLocalRandom.current().nextDouble() <= 2D / 1000D;
    }
}
