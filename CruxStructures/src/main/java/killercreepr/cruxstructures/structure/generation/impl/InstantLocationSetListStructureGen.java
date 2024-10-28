package killercreepr.cruxstructures.structure.generation.impl;

import killercreepr.crux.Crux;
import killercreepr.crux.data.Pos2D;
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
import java.util.logging.Level;

public class InstantLocationSetListStructureGen extends LocationSetListStructureGen {
    protected final @NotNull String id;
    public InstantLocationSetListStructureGen(@NotNull List<StructureGenerator> structurePool, @Nullable NumberProvider chunkRangeX, @Nullable NumberProvider chunkRangeZ, @Nullable NumberProvider minDistanceApart, @NotNull String id) {
        super(structurePool,  chunkRangeX, chunkRangeZ, minDistanceApart);
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

        Crux.log(Level.INFO, world.getName() + " - Instant set location list is now processing. " + id);
        new BukkitRunnable(){
            int index = -1;
            @Override
            public void run() {
                index++;
                Pos2D pos = listChunks.get(index);
                if((index+1) >= listChunks.size()){
                    cancel();
                    Crux.log(Level.INFO, world.getName() + " - Instant set location list has finished. " + id);
                }

                if(world.isChunkGenerated(pos.x(), pos.z()) && !(at.getX() == pos.x() && at.getZ() == pos.z())) return;
                world.getChunkAtAsync(pos.x(), pos.z()).whenComplete((chunk, throwable) ->{
                    List<StructureGenerator> populated = populateLoot(at);
                    if(populated.isEmpty()) return;
                    StructureGenerator gen = populated.getFirst();
                    gen.generate(chunk);
                });
            }
        }.runTaskTimerAsynchronously(Crux.getMainPlugin(), 100L, 200L);

        CruxTag.set(world, "instant_location_set/" + id, PersistentDataType.BOOLEAN, true);

        return GenerateResult.empty();
    }
}
