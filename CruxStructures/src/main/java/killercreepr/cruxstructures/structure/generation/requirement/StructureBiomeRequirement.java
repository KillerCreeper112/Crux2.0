package killercreepr.cruxstructures.structure.generation.requirement;

import killercreepr.crux.paper.nms.biome.BiomeUtils;
import killercreepr.cruxstructures.structure.Structure;
import net.kyori.adventure.key.Key;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class StructureBiomeRequirement implements StructureRequirement {
    protected final @NotNull Key biomeKey;
    public StructureBiomeRequirement(@NotNull Key biomeKey) {
        this.biomeKey = biomeKey;
    }

    @Override
    public boolean test(@NotNull Structure structure, @NotNull Chunk chunk, @NotNull Location location) {
        return BiomeUtils.getBiome(location).equals(biomeKey);
    }
}
