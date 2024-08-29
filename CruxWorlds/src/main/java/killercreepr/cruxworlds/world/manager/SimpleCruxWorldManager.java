package killercreepr.cruxworlds.world.manager;

import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import killercreepr.cruxworlds.registry.ActiveCruxWorldRegistry;
import killercreepr.cruxworlds.world.CruxWorld;
import killercreepr.cruxworlds.world.SimpleWorld;
import killercreepr.cruxworlds.world.creator.CruxWorldCreator;
import killercreepr.cruxworlds.world.creator.CruxWorldModuleCreator;
import killercreepr.cruxworlds.world.creator.WorldModuleCreatorRegistry;
import killercreepr.cruxworlds.world.creator.WorldModuleCreatorRegistryImpl;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public class SimpleCruxWorldManager implements CruxWorldManager, Listener {
    protected final @NotNull MappedRegistry<String, CruxWorldCreator> creators = new SimpleMappedRegistry<>();
    protected final @NotNull WorldModuleCreatorRegistry moduleCreators = new WorldModuleCreatorRegistryImpl();
    protected final @NotNull ActiveCruxWorldRegistry active = new ActiveCruxWorldRegistry();
    @Override
    public @Nullable CruxWorld getWorld(@NotNull String name) {
        return active.getByName(name);
    }

    @Override
    public @Nullable CruxWorld getWorld(@NotNull UUID uuid) {
        return active.get(uuid);
    }

    @Override
    public @NotNull Collection<CruxWorld> getWorlds() {
        return active.values();
    }

    @Override
    public @NotNull MappedRegistry<String, CruxWorldCreator> getCreatorRegistry() {
        return creators;
    }

    @Override
    public @NotNull WorldModuleCreatorRegistry getModuleCreatorRegistry() {
        return moduleCreators;
    }

    public @Nullable CruxWorld create(@NotNull World world){
        String name = world.getName();
        CruxWorldCreator creator = creators.get(name);
        Collection<CruxWorldModuleCreator> moduleCreators = this.moduleCreators.get(name);
        CruxWorld cruxWorld;
        if(creator == null){
            if(moduleCreators == null || moduleCreators.isEmpty()) return null;
            cruxWorld = new SimpleWorld(world, moduleCreators);

        }else cruxWorld = creator.create(world);
        return cruxWorld;
    }

    @EventHandler(ignoreCancelled = true)
    public void onWorldInit(WorldInitEvent event) {
        World world = event.getWorld();

        CruxWorld crux = create(world);
        if(crux==null) return;
        active.register(crux);
        crux.onInitiate();
    }

    @EventHandler(ignoreCancelled = true)
    public void onWorldLoad(WorldLoadEvent event) {
        World world = event.getWorld();
        CruxWorld crux = getWorld(world.getUID());
        if(crux==null) return;
        crux.onLoad();
    }

    @EventHandler(ignoreCancelled = true)
    public void onWorldUnload(WorldUnloadEvent event) {
        World world = event.getWorld();
        CruxWorld crux = getWorld(world.getUID());
        if(crux==null) return;
        active.remove(crux.getUUID());
        crux.onUnload();
    }

    @EventHandler(ignoreCancelled = true)
    public void onWorldSave(WorldSaveEvent event) {
        World world = event.getWorld();
        CruxWorld crux = getWorld(world.getUID());
        if(crux==null) return;
        crux.onSave();
    }

    public @NotNull MappedRegistry<String, CruxWorldCreator> getCreators() {
        return creators;
    }

    public @NotNull ActiveCruxWorldRegistry getActive() {
        return active;
    }
}
