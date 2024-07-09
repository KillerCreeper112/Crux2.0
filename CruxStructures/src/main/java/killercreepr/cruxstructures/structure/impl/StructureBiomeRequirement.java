package killercreepr.cruxstructures.structure.impl;

import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.StructureRequirement;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;

public class StructureBiomeRequirement implements StructureRequirement {
    protected final @NotNull Biome biome;
    public StructureBiomeRequirement(@NotNull Biome biome) {
        this.biome = biome;
    }

    @Override
    public boolean test(@NotNull Structure structure, @NotNull Chunk chunk, @NotNull Location location) {
        return location.getBlock().getBiome() == biome;
    }
}
