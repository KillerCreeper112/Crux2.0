package killercreepr.cruxstructures.core.structure.generation;

import killercreepr.crux.api.math.Pos2D;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.util.CruxTag;
import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.api.structure.generation.StructureGenerator;
import killercreepr.cruxstructures.api.structure.generation.result.GenerateResult;
import net.kyori.adventure.key.Key;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class InstantLocationSetListStructureGen extends LocationSetListStructureGen {
    protected final @NotNull String id;
    public InstantLocationSetListStructureGen(@NotNull List<StructureGenerator> structurePool, @Nullable NumberProvider chunkRangeX, @Nullable NumberProvider chunkRangeZ, @Nullable NumberProvider minDistanceApart, @NotNull String id) {
        super(structurePool,  chunkRangeX, chunkRangeZ, minDistanceApart);
        this.id = id;
    }

    public @NotNull String getId() {
        return id;
    }

    public boolean hasGeneratedIn(@NotNull World world){
        return CruxTag.has(world, "instant_location_set/" + id);
    }

    public Map<Key, BukkitRunnable> getAlreadyGenerated() {
        return alreadyGenerated;
    }

    protected final Map<Key, BukkitRunnable> alreadyGenerated = new HashMap<>();

    @Override
    public @NotNull CompletableFuture<GenerateResult> generate(@NotNull Structure structure, @NotNull Chunk at) {
        if(setChunks != null) return CompletableFuture.completedFuture(GenerateResult.empty());
        World world = at.getWorld();
        if(hasGeneratedIn(world) || alreadyGenerated.containsKey(world.key())) return CompletableFuture.completedFuture(GenerateResult.empty());
        Crux.scheduler().runTask(() ->{
            if(setChunks != null || alreadyGenerated.containsKey(world.key()) || hasGeneratedIn(world)) return;
            setChunks = generateSetChunks(world, minDistanceApart == null ? 0 : minDistanceApart.value().intValue());
            if(setChunks == null) return;
            CruxTag.set(world, "instant_location_set/" + id, PersistentDataType.BOOLEAN, true);

            List<Pos2D> listChunks = new ArrayList<>(setChunks);

            Crux.log(Level.INFO, world.getName() + " - Instant set location list is now processing. " + id);
            BukkitRunnable runnable = new BukkitRunnable(){
                int index = -1;
                @Override
                public void run() {
                    double tps = Crux.getServer().getTPS()[0];
                    if(tps < 14.3){
                        Crux.log(Level.INFO, world.getName() + " - Instant set location list " + id +
                          ": TPS low, skipping... " + tps);
                        return;
                    }
                    index++;
                    Pos2D pos = listChunks.get(index);
                    if((index+1) >= listChunks.size()){
                        cancel();
                        Crux.log(Level.INFO, world.getName() + " - Instant set location list has finished. " + id);
                        alreadyGenerated.remove(world.key());
                        onComplete(world, at);
                    }

                    if(world.isChunkGenerated(pos.x(), pos.z()) && !(at.getX() == pos.x() && at.getZ() == pos.z())) return;
                    world.getChunkAtAsync(pos.x(), pos.z()).whenComplete((chunk, throwable) ->{
                        List<StructureGenerator> populated = populateLoot(at);
                        if(populated.isEmpty()) return;
                        StructureGenerator gen = populated.getFirst();
                        var genStructure = gen.generateStructure(chunk);
                        if(genStructure != null) gen.generate(genStructure, chunk);
                    });
                }
            };
            alreadyGenerated.put(world.key(), runnable);
            runnable.runTaskTimerAsynchronously(Crux.getMainPlugin(), 100L, 300L);
        });

        return CompletableFuture.completedFuture(GenerateResult.empty());
    }

    public void onComplete(World world, Chunk chunk){

    }
}
