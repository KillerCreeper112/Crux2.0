package killercreepr.cruxstructures.structure.generation.requirement;

import killercreepr.crux.util.CruxMath;
import killercreepr.cruxstructures.structure.Structure;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

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
