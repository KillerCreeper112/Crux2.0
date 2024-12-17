package killercreepr.cruxstructures.manager;

import killercreepr.cruxstructures.event.StructurePlaceEvent;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.stored.StoredStructure;
import killercreepr.cruxworlds.api.event.CruxWorldPreCreateEvent;
import killercreepr.cruxworlds.api.world.CruxWorld;
import killercreepr.cruxworlds.api.world.manager.CruxWorldManager;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class StructureManager implements Listener {
    protected final @NotNull Plugin plugin;
    protected final @NotNull CruxWorldManager worldManager;
    public StructureManager(@NotNull Plugin plugin, @NotNull CruxWorldManager worldManager) {
        this.plugin = plugin;
        this.worldManager = worldManager;
    }

    public @NotNull Plugin getPlugin() {
        return plugin;
    }

    public @NotNull CruxWorldManager getWorldManager() {
        return worldManager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCruxWorldPreCreate(CruxWorldPreCreateEvent event) {
        event.getModuleCreators().add(StructureWorldModule::new);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onStructurePlace(StructurePlaceEvent event) {
        Structure structure = event.getStructure();
        if(!structure.isPersistent()) return;
        Location spawn = event.getLocation();
        StoredStructure stored = structure.buildStored(spawn, event.getRotation());
        if(stored==null) return;
        if(!stored.shouldPersist()) return;

        CruxWorld world = worldManager.getWorld(spawn.getWorld().getUID());
        if(world == null) return;
        StructureWorldModule module = world.getModule(StructureWorldModule.class);
        if(module == null) return;
        Chunk chunk = spawn.getChunk();
        module.addStoredStructure(stored, chunk);
    }
}
