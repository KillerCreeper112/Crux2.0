package killercreepr.cruxstructures.core.structure.generation.requirement;

import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.api.structure.generation.StructureRequirement;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class StructureChanceRequirement implements StructureRequirement {
    protected final float chance;
    public StructureChanceRequirement(float chance) {
        this.chance = chance;
    }

    @Override
    public CompletableFuture<Boolean> test(@NotNull Structure structure, @NotNull Chunk chunk, @NotNull Location location) {
        return CompletableFuture.completedFuture(CruxMath.testChance(chance));
    }
}
