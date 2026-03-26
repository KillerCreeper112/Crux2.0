package killercreepr.cruxstructures.core.structure.generation;

import killercreepr.crux.api.loot.LootTable;
import killercreepr.crux.api.math.Pos2D;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.util.CruxTag;
import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.api.structure.generation.StructureGenerator;
import killercreepr.cruxstructures.api.structure.generation.result.GenerateResult;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class InstantLocationSetTableStructureGen extends LocationSetTableStructureGen {
    protected final @NotNull String id;
    public InstantLocationSetTableStructureGen(@NotNull LootTable<StructureGenerator> structurePool, @NotNull NumberProvider structureAmount, @Nullable NumberProvider chunkRangeX, @Nullable NumberProvider chunkRangeZ, @Nullable NumberProvider minDistanceApart, @NotNull String id) {
        super(structurePool, structureAmount, chunkRangeX, chunkRangeZ, minDistanceApart);
        this.id = id;
    }

    public boolean hasGeneratedIn(@NotNull World world){
        return CruxTag.has(world, "instant_location_set/" + id);
    }

    @Override
    public @NotNull CompletableFuture<GenerateResult> generate(@NotNull Structure structure, @NotNull Chunk at) {
        if(setChunks != null || hasGeneratedIn(at.getWorld())) return CompletableFuture.completedFuture(GenerateResult.empty());
        setChunks = generateSetChunks(at.getWorld(), minDistanceApart == null ? 0 : minDistanceApart.value().intValue());
        if(setChunks == null) return CompletableFuture.completedFuture(GenerateResult.empty());

        List<Pos2D> listChunks = new ArrayList<>(setChunks);
        World world = at.getWorld();

        Crux.log(Level.INFO, world.getName() + " - Instant set location table is now processing. " + id);
        new BukkitRunnable(){
            int index = -1;
            @Override
            public void run() {
                index++;
                Pos2D pos = listChunks.get(index);
                if((index+1) >= listChunks.size()){
                    cancel();
                    Crux.log(Level.INFO, world.getName() + " - Instant set location table has finished. " + id);
                }
                final var finalIndex = index;

                if(world.isChunkGenerated(pos.x(), pos.z()) && !(at.getX() == pos.x() && at.getZ() == pos.z())) return;
                Chunk chunk = world.getChunkAt(pos.x(), pos.z());
                List<StructureGenerator> populated = populateLoot(at);
                Crux.logInfo(finalIndex + " - " + world.getName() + " - Instant set location table structure generating: " + populated.size());
                if(populated.isEmpty()) return;
                StructureGenerator gen = populated.getFirst();
                var genStructure = gen.generateStructure(chunk);
                if(genStructure != null){
                    gen.generate(genStructure, chunk).thenAccept(result -> {
                        Crux.logInfo(finalIndex + " - " + world.getName() + " - Instant set location table structure generated!");
                    });
                }
            }
        }.runTaskTimer(Crux.getMainPlugin(), 100L, 100L);

        CruxTag.set(world, "instant_location_set/" + id, PersistentDataType.BOOLEAN, true);

        return CompletableFuture.completedFuture(GenerateResult.empty());
    }
}
