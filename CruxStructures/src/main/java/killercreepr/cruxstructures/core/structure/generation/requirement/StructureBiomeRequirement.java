package killercreepr.cruxstructures.core.structure.generation.requirement;

import killercreepr.crux.paper.nms.biome.BiomeUtils;
import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.api.structure.generation.StructureRequirement;
import net.kyori.adventure.key.Key;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class StructureBiomeRequirement implements StructureRequirement {
    protected final @NotNull Key biomeKey;
    public StructureBiomeRequirement(@NotNull Key biomeKey) {
        this.biomeKey = biomeKey;
    }

    @Override
    public CompletableFuture<Boolean> test(@NotNull Structure structure, @NotNull Chunk chunk, @NotNull Location location) {
        return CompletableFuture.completedFuture(BiomeUtils.getBiome(location).equals(biomeKey));
    }
}
