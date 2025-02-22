package killercreepr.cruxstructures.core.manager;

import killercreepr.crux.api.data.Reloadable;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxstructures.api.event.StructurePlaceEvent;
import killercreepr.cruxstructures.api.structure.StoredStructure;
import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.api.world.module.StructureWorldModule;
import killercreepr.cruxstructures.core.CruxStructuresModule;
import killercreepr.cruxstructures.core.config.loader.StructureLoader;
import killercreepr.cruxstructures.core.registries.StructureRegistries;
import killercreepr.cruxstructures.core.structure.CfgFAWEStructure;
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

import java.util.HashSet;

public class StructureManager implements Listener, Reloadable {
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

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onCruxWorldPreCreate(CruxWorldPreCreateEvent event) {
        event.getModuleCreators().add(SimpleStructureWorldModule::new);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onStructurePlace(StructurePlaceEvent event) {
        Structure structure = event.getStructure();
        if(!structure.isPersistent()) return;
        Location spawn = event.getLocation();
        StoredStructure stored = structure.buildStored(spawn, event.getRotation());
        if(stored==null) return;
        if(!stored.shouldPersist()) return;

        CruxWorld world = worldManager.getWorld(spawn.getWorld().key());
        if(world == null) return;
        StructureWorldModule module = world.getModule(StructureWorldModule.class);
        if(module == null) return;
        Chunk chunk = spawn.getChunk();
        module.addStoredStructure(stored, chunk);
    }

    public @NotNull CruxFolder createStructuresFolder(){
        return new CruxFolder(Crux.getMainPlugin(), "structures");
    }

    public void loadStructureConfiguration(){
        CruxFolder cfgFolder = createStructuresFolder();
        new HashSet<>(StructureRegistries.STRUCTURES.values()).forEach(str ->{
            if(str instanceof CfgFAWEStructure){
                StructureRegistries.STRUCTURES.remove(str.key());
            }
        });

        new StructureLoader(CruxRegistries.MODULES.getModuleOrThrow(CruxStructuresModule.class).getFileCfgFAWEStructure())
            .loadConfiguration(cfgFolder.file());
    }

    @Override
    public void reload(@NotNull CruxPlugin plugin) {
        loadStructureConfiguration();
    }
}
