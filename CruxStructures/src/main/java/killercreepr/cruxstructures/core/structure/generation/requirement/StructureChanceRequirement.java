package killercreepr.cruxstructures.core.structure.generation.requirement;

import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxstructures.api.structure.generation.StructureRequirement;
import killercreepr.cruxstructures.api.structure.Structure;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class StructureChanceRequirement implements StructureRequirement {
    protected final float chance;
    public StructureChanceRequirement(float chance) {
        this.chance = chance;
    }

    @Override
    public boolean test(@NotNull Structure structure, @NotNull Chunk chunk, @NotNull Location location) {
        return CruxMath.testChance(chance);
    }
}
