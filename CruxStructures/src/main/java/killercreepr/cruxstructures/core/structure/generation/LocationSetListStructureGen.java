package killercreepr.cruxstructures.core.structure.generation;

import killercreepr.crux.api.text.context.InputContext;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxstructures.api.structure.generation.StructureGenerator;
import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LocationSetListStructureGen extends LocationSetStructureGen{
    protected final @NotNull List<StructureGenerator> structurePool;
    protected final @NotNull List<StructureGenerator> currentPool;
    public LocationSetListStructureGen(@NotNull List<StructureGenerator> structurePool, @Nullable NumberProvider chunkRangeX, @Nullable NumberProvider chunkRangeZ, @Nullable NumberProvider minDistanceApart) {
        super(chunkRangeX, chunkRangeZ, minDistanceApart);
        this.structurePool = structurePool;
        this.currentPool = new ArrayList<>(structurePool);
    }

    public @NotNull List<StructureGenerator> getStructurePool() {
        return structurePool;
    }

    public @NotNull List<StructureGenerator> getCurrentPool() {
        return currentPool;
    }

    @Override
    public List<StructureGenerator> populateLoot(@NotNull Chunk at) {
        if(currentPool.isEmpty()) return List.of();
        int index = CruxMath.random(0, currentPool.size()-1);
        StructureGenerator gen = currentPool.remove(index);
        return List.of(gen);
    }

    @Override
    public int getGenerateStructureAmount(@NotNull InputContext ctx) {
        return currentPool.size();
    }
}
