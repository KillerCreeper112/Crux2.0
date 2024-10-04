package killercreepr.cruxstructures.structure.impl;

import killercreepr.crux.Crux;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Pos2D;
import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.LootTable;
import killercreepr.crux.util.CruxTag;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxstructures.structure.generation.StructureGenerator;
import killercreepr.cruxstructures.structure.result.GenerateResult;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InstantLocationSetStructureGen extends LocationSetStructureGen {
    protected final @NotNull String id;
    public InstantLocationSetStructureGen(@NotNull LootTable<StructureGenerator> structurePool, @NotNull NumberProvider structureAmount, @Nullable NumberProvider chunkRangeX, @Nullable NumberProvider chunkRangeZ, @Nullable NumberProvider minDistanceApart, @NotNull String id) {
        super(structurePool, structureAmount, chunkRangeX, chunkRangeZ, minDistanceApart);
        this.id = id;
    }

    public boolean hasGeneratedIn(@NotNull World world){
        return CruxTag.has(world, "instant_location_set/" + id);
    }

    @Override
    public @NotNull GenerateResult generate(@NotNull Chunk at) {
        if(setChunks != null || hasGeneratedIn(at.getWorld())) return GenerateResult.empty();
        setChunks = generateSetChunks(at.getWorld(), minDistanceApart == null ? 0 : minDistanceApart.value().intValue());
        if(setChunks == null) return GenerateResult.empty();

        List<Pos2D> listChunks = new ArrayList<>(setChunks);
        World world = at.getWorld();

        new BukkitRunnable(){
            int index = -1;
            @Override
            public void run() {
                index++;
                Pos2D pos = listChunks.get(index);
                if((index+1) >= listChunks.size()){
                    cancel();
                }

                if(world.isChunkGenerated(pos.x(), pos.z()) && !(at.getX() == pos.x() && at.getZ() == pos.z())) return;
                Chunk chunk = world.getChunkAt(pos.x(), pos.z());
                List<StructureGenerator> populated = structurePool.populateLoot(LootContext.builder()
                    .info(DataExchange.builder().put("chunk", at).build()).build());
                if(populated.isEmpty()) return;
                StructureGenerator gen = populated.getFirst();
                gen.generate(chunk);
            }
        }.runTaskTimer(Crux.getMainPlugin(), 100L, 200L);

        /*for(Pos2D pos : setChunks){
            if(world.isChunkGenerated(pos.x(), pos.z()) && !(at.getX() == pos.x() && at.getZ() == pos.z())) continue;
            Chunk chunk = world.getChunkAt(pos.x(), pos.z());
            List<StructureGenerator> populated = structurePool.populateLoot(LootContext.builder()
                .info(DataExchange.builder().put("chunk", at).build()).build());
            if(populated.isEmpty()) continue;
            StructureGenerator gen = populated.getFirst();
            lastResult = gen.generate(chunk);
        }*/

        CruxTag.set(world, "instant_location_set/" + id, PersistentDataType.BOOLEAN, true);

        return GenerateResult.empty();
    }
}
