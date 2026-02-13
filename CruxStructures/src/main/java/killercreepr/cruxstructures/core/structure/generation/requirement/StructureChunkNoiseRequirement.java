package killercreepr.cruxstructures.core.structure.generation.requirement;

import killercreepr.cruxgeneration.util.CruxNoise;
import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.api.structure.generation.StructureChunkRequirement;
import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;

public class StructureChunkNoiseRequirement implements StructureChunkRequirement {
    protected final CruxNoise noise;
    protected final float min;
    protected final float max;

    public StructureChunkNoiseRequirement(CruxNoise noise, float min, float max) {
        this.noise = noise;
        this.min = min;
        this.max = max;
    }

    public StructureChunkNoiseRequirement(float min, float max, float noiseFrequency, int noiseOctaves){
        this(
            CruxNoise.fast()
                .frequency(noiseFrequency)
                .noiseType(CruxNoise.NoiseType.OpenSimplex2)
                .fractalType(CruxNoise.FractalType.FBm)
                .fractalOctaves(noiseOctaves),
            min, max
        );
    }

    public CruxNoise getNoise() {
        return noise;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    @Override
    public boolean test(@NotNull Structure structure, @NotNull Chunk chunk) {
        double x = noise.noise(chunk.getX(), chunk.getZ());
        return x >= min && x <= max;
    }
}
