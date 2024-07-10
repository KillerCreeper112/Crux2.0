package killercreepr.cruxstructures.manager;

import killercreepr.crux.data.BlockPos;
import killercreepr.crux.data.world.MultiVerseWorldStorage;
import killercreepr.crux.data.world.WorldChunkStorage;
import killercreepr.crux.data.world.standard.MultiVerseBlockPosedStorage;
import killercreepr.cruxstructures.structure.GenerateResult;
import killercreepr.cruxstructures.structure.active.ActiveStructure;
import killercreepr.cruxstructures.structure.impl.CfgStructureGen;
import killercreepr.cruxstructures.structure.stored.StoredStructure;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class StructureManager implements Listener {
    //world name -> structure gens
    protected final @NotNull Map<String, List<CfgStructureGen>> structures = new HashMap<>();

    protected final @NotNull MultiVerseWorldStorage<StoredStructure> stored = new MultiVerseBlockPosedStorage<>();
    protected final @NotNull MultiVerseWorldStorage<ActiveStructure> active = new MultiVerseBlockPosedStorage<>();

    @EventHandler(ignoreCancelled = true)
    public void onChunkPopulate(ChunkPopulateEvent event) {
        Chunk c = event.getChunk();
        List<CfgStructureGen> list = structures.get(c.getWorld().getName());
        if(list==null) return;
        //landStructuresPerThousandChunks = 2
        if (ThreadLocalRandom.current().nextDouble() > 2D / 1000D) return;
        Collections.shuffle(list);
        for(CfgStructureGen gen : list){
            GenerateResult result = gen.generate(c);
            if(result.getPlaceEvent() == null || result.getPlaceEvent().isCancelled()) continue;
            break;
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onWorldLoad(WorldLoadEvent event) {
    }

    @EventHandler(ignoreCancelled = true)
    public void onWorldInit(WorldInitEvent event) {
    }

    @EventHandler(ignoreCancelled = true)
    public void onWorldUnload(WorldUnloadEvent event) {
        World world = event.getWorld();
        UUID worldUUID = world.getUID();

        WorldChunkStorage<StoredStructure> removed = stored.remove(worldUUID);
        if(removed==null) return;

    }

    @EventHandler(ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();
        UUID worldUUID = chunk.getWorld().getUID();
        long key = chunk.getChunkKey();
        WorldChunkStorage<StoredStructure> stored = this.stored.get(worldUUID);
        if(stored==null) return;

        stored.getData().values().forEach(structures ->{
            structures.getData().values().forEach(st ->{
                ActiveStructure active = st.buildActive(chunk);
                if(active==null) return;
                this.active.add(
                    worldUUID, key, active
                );
            });
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        Chunk chunk = event.getChunk();
        UUID worldUUID = chunk.getWorld().getUID();
        long key = chunk.getChunkKey();
        WorldChunkStorage<ActiveStructure> active = this.active.get(worldUUID);
        if(active==null) return;

        active.getData().values().forEach(structures ->{
            structures.getData().values().forEach(a ->{
                StoredStructure data = a.save();
                BlockPos center = a.getData().getBlockPos();
                if(data == null) stored.remove(chunk.getWorld().getUID(), key, center);
                else stored.add(chunk.getWorld().getUID(), key, data);
            });
        });
        this.active.remove(worldUUID, key);
    }
}
