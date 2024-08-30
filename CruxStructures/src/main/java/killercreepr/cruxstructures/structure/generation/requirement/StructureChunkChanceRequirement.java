package killercreepr.cruxstructures.structure.generation.requirement;

import killercreepr.crux.util.CruxMath;
import killercreepr.cruxstructures.structure.Structure;
import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class StructureChunkChanceRequirement implements StructureChunkRequirement {
    protected final float chance;
    public StructureChunkChanceRequirement(float chance) {
        this.chance = chance;
    }

    @Override
    public boolean test(@NotNull Structure structure, @NotNull Chunk chunk) {
        return CruxMath.testChance(new Random(chunk.getWorld().getSeed()), chance);
    }
}
