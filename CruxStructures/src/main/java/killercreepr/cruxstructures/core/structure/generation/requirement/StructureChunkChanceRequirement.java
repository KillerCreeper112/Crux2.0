package killercreepr.cruxstructures.core.structure.generation.requirement;

import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.api.structure.generation.StructureChunkRequirement;
import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;

public class StructureChunkChanceRequirement implements StructureChunkRequirement {
    protected final float chance;
    public StructureChunkChanceRequirement(float chance) {
        this.chance = chance;
    }

    @Override
    public boolean test(@NotNull Chunk chunk) {
        return CruxMath.testChance(chance);
    }
}
