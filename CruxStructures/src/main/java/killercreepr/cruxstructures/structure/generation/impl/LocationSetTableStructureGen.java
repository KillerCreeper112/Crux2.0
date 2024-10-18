package killercreepr.cruxstructures.structure.generation.impl;

import killercreepr.crux.context.InputContext;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.LootTable;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxstructures.structure.generation.StructureGenerator;
import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LocationSetTableStructureGen extends LocationSetStructureGen{
    protected final @NotNull LootTable<StructureGenerator> structurePool;
    protected final @NotNull NumberProvider structureAmount;
    public LocationSetTableStructureGen(@NotNull LootTable<StructureGenerator> structurePool, @NotNull NumberProvider structureAmount, @Nullable NumberProvider chunkRangeX, @Nullable NumberProvider chunkRangeZ, @Nullable NumberProvider minDistanceApart) {
        super(chunkRangeX, chunkRangeZ, minDistanceApart);
        this.structurePool = structurePool;
        this.structureAmount = structureAmount;
    }

    @Override
    public List<StructureGenerator> populateLoot(@NotNull Chunk at) {
        return structurePool.populateLoot(LootContext.builder()
            .info(DataExchange.builder().put("chunk", at).build()).build());
    }

    @Override
    public int getGenerateStructureAmount(@NotNull InputContext ctx) {
        return structureAmount.sample(ctx).intValue();
    }

    public @NotNull LootTable<StructureGenerator> getStructurePool() {
        return structurePool;
    }

    public @NotNull NumberProvider getStructureAmount() {
        return structureAmount;
    }
}
