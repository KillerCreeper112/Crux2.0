package killercreepr.cruxstructures.manager;

import killercreepr.cruxstructures.structure.GenerateResult;
import killercreepr.cruxstructures.structure.impl.CfgStructureGen;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class StructureManager implements Listener {
    //world name -> structure gens
    protected final @NotNull Map<String, List<CfgStructureGen>> structures = new HashMap<>();
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
}
