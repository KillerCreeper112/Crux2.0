package killercreepr.cruxblocks.core.block.manager;

import killercreepr.cruxblocks.api.world.module.CruxBlocksWorldModule;
import killercreepr.cruxworlds.api.world.CruxWorld;
import killercreepr.cruxworlds.api.world.manager.CruxWorldManager;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class SimpleCruxBlockManager implements Listener {
    protected final CruxWorldManager worldManager;
    public SimpleCruxBlockManager(CruxWorldManager worldManager) {
        this.worldManager = worldManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();
        CruxWorld world = worldManager.getWorld(event.getWorld().getUID());
        if(world == null) return;
        CruxBlocksWorldModule module = world.getModule(CruxBlocksWorldModule.class);
        if(module == null) return;
        module.onChunkLoad(chunk);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        Chunk chunk = event.getChunk();
        CruxWorld world = worldManager.getWorld(event.getWorld().getUID());
        if(world == null) return;
        CruxBlocksWorldModule module = world.getModule(CruxBlocksWorldModule.class);
        if(module == null) return;
        module.onChunkUnload(chunk);
    }
}
