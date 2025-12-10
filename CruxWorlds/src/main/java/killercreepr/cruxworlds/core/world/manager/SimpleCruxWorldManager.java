package killercreepr.cruxworlds.core.world.manager;

import killercreepr.crux.api.data.tick.ManagedTicked;
import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.crux.api.registry.MappedRegistry;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.registry.SimpleMappedRegistry;
import killercreepr.crux.core.util.CruxWorldUtil;
import killercreepr.cruxworlds.api.event.CruxWorldDeleteEvent;
import killercreepr.cruxworlds.api.event.CruxWorldPreCreateEvent;
import killercreepr.cruxworlds.api.world.CruxWorld;
import killercreepr.cruxworlds.api.world.creator.CruxWorldCreator;
import killercreepr.cruxworlds.api.world.creator.CruxWorldModuleCreator;
import killercreepr.cruxworlds.api.world.creator.WorldModuleCreatorRegistry;
import killercreepr.cruxworlds.api.world.manager.CruxWorldManager;
import killercreepr.cruxworlds.api.world.type.CruxWorldType;
import killercreepr.cruxworlds.core.component.CruxWorldsComponents;
import killercreepr.cruxworlds.core.registries.WorldsRegistries;
import killercreepr.cruxworlds.core.registry.ActiveCruxWorldRegistry;
import killercreepr.cruxworlds.core.world.SimpleWorld;
import killercreepr.cruxworlds.core.world.creator.WorldModuleCreatorRegistryImpl;
import killercreepr.cruxworlds.core.world.type.DefaultWorldType;
import net.kyori.adventure.key.Key;
import org.bukkit.Chunk;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleCruxWorldManager implements CruxWorldManager, Listener {
    protected final Plugin plugin;
    protected final Server server;

    public SimpleCruxWorldManager(Plugin plugin, Server server) {
        this(plugin, server, WorldsRegistries.WORLD_TYPE);
    }

    public SimpleCruxWorldManager(Plugin plugin) {
        this(plugin, plugin.getServer());
    }

    public SimpleCruxWorldManager(Plugin plugin, Server server, @NotNull KeyedRegistry<CruxWorldType> worldTypes) {
        this.plugin = plugin;
        this.server = server;
        this.worldTypes = worldTypes;
    }
    protected final @NotNull KeyedRegistry<CruxWorldType> worldTypes;
    protected final @NotNull MappedRegistry<Key, CruxWorldCreator> creators = new SimpleMappedRegistry<>();
    protected final @NotNull WorldModuleCreatorRegistry moduleCreators = new WorldModuleCreatorRegistryImpl();
    protected final @NotNull ActiveCruxWorldRegistry active = new ActiveCruxWorldRegistry(new ConcurrentHashMap<>());

    public BukkitRunnable buildRunnable(){
        return new BukkitRunnable() {
            @Override
            public void run() {
                /*if(!isActive()){
                    cancel();
                    return;
                }*/
                tick();
            }
        };
    }


    Collection<CruxWorld> toStop = new HashSet<>();
    public void tick(){
        toStop.clear();
        active.getTicked().forEach(tick ->{
            if(tick.shouldStop()){
                tick.stopped();
                if(tick instanceof CruxWorld w && w.scheduledUnload()){
                    toStop.add(w);
                }
                return; //true;
            }
            tick.tick();
            //return false;
        });
        if(toStop.isEmpty()) return;
        for (CruxWorld crux : toStop) {
            if(crux instanceof ManagedTicked ticked){
                active.removeTicked(ticked);
            }
        }
        Collection<CruxWorld> copied = new HashSet<>(toStop);
        Crux.scheduler().runTaskMain(() ->{
            for (CruxWorld w : copied) {
                unloadWorld(w, w.scheduledUnloadSave());
            }
        });
    }

    protected final Map<Key, CruxWorldType> defaultWorldTypes = new HashMap<>();
    @Override
    public CruxWorldType getWorldType(@NotNull World world) {
        var crux = getWorld(world.key());
        if(crux == null) return null;
        return getWorldType(crux);
    }

    @Override
    public CruxWorldType getWorldType(@NotNull CruxWorld world) {
        CruxWorldType type = world.get(CruxWorldsComponents.WORLD_TYPE);
        if(type != null) return type;

        Key environmentKey = Key.key(world.toBukkitWorld().getEnvironment().toString().toLowerCase());
        type = defaultWorldTypes.get(environmentKey);
        if(type != null) return type;

        type = new DefaultWorldType(environmentKey, this, world.toBukkitWorld().getEnvironment());
        defaultWorldTypes.put(environmentKey, type);
        return type;
    }

    @Override
    public @Nullable CruxWorld getOrCreateWorld(@NotNull Key worldType, @NotNull Key name) {
        CruxWorld world = getWorld(name);
        if(world != null) return world;
        CruxWorldType type = worldTypes.get(worldType);
        if(type == null) return null;
        return getOrCreateWorld(type, name);
    }

    @Override
    public @Nullable CruxWorld getOrCreateWorld(@NotNull CruxWorldType type, @NotNull Key name) {
        CruxWorld world = getWorld(name);
        if(world != null) return world;
        CruxWorld crux = type.generate(name);
        crux.set(CruxWorldsComponents.WORLD_TYPE, type);
        return crux;
    }

    @Override
    public @Nullable CruxWorld getWorld(@NotNull Key name) {
        return active.get(name);
    }

    /*@Override
    public @Nullable CruxWorld getWorld(@NotNull String name) {
        return active.getByName(name);
    }

    @Override
    public @Nullable CruxWorld getWorld(@NotNull UUID uuid) {
        return active.get(uuid);
    }*/

    @Override
    public CompletableFuture<Boolean> deleteWorld(@NotNull CruxWorld world) {
        if(!world.toBukkitWorld().getPlayers().isEmpty()) return CompletableFuture.completedFuture(false);
        world.onPreDelete();
        unloadWorld(world, false);
        boolean x = CruxWorldUtil.deleteWorld(world.toBukkitWorld());
        if(x){
            new CruxWorldDeleteEvent(world).callEvent();
            world.onDelete();
        }
        return CompletableFuture.completedFuture(x);
    }

    @Override
    public CompletableFuture<Boolean> deleteWorld(@NotNull Key world) {
        CruxWorld crux = getWorld(world);
        if(crux != null) return deleteWorld(crux);
        return CompletableFuture.completedFuture(CruxWorldUtil.deleteWorld(world.value()));
    }

    @Override
    public CompletableFuture<Boolean> unloadWorld(@NotNull CruxWorld world, boolean save) {
        world.setShouldSaveOnNextUnload(save);
        return CompletableFuture.completedFuture(server.unloadWorld(world.toBukkitWorld(), save));
    }

    @Override
    public CompletableFuture<CruxWorld> loadWorld(@NotNull Key worldName) {
        CruxWorldUtil.getOrLoadWorld(worldName.value());
        return CompletableFuture.completedFuture(getWorld(worldName));
    }

    @Override
    public @NotNull Collection<CruxWorld> getWorlds() {
        return active.values();
    }

    @Override
    public @NotNull MappedRegistry<Key, CruxWorldCreator> getCreatorRegistry() {
        return creators;
    }

    @Override
    public @NotNull KeyedRegistry<CruxWorldType> getWorldTypeRegistry() {
        return worldTypes;
    }

    @Override
    public @NotNull WorldModuleCreatorRegistry getModuleCreatorRegistry() {
        return moduleCreators;
    }

    public @Nullable CruxWorld create(@NotNull World world){
        CruxWorldCreator creator = creators.get(world.key());
        Collection<CruxWorldModuleCreator> moduleCreators = this.moduleCreators.get(world.key());
        if(moduleCreators == null) moduleCreators = new HashSet<>();
        else moduleCreators = new HashSet<>(moduleCreators);

        CruxWorldPreCreateEvent event = new CruxWorldPreCreateEvent(world, moduleCreators);
        if(!event.callEvent()) return null;

        CruxWorld cruxWorld;
        if(creator == null){
            cruxWorld = new SimpleWorld(world, moduleCreators);
        }else cruxWorld = creator.create(world, moduleCreators);
        if(!cruxWorld.has(CruxWorldsComponents.WORLD_CREATED_AT_TIME)){
            cruxWorld.set(CruxWorldsComponents.WORLD_CREATED_AT_TIME, System.currentTimeMillis());
        }
        cruxWorld.onCreate();
        return cruxWorld;
    }

    @EventHandler(ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent event) {
        if(event.isNewChunk()) return;
        World world = event.getWorld();
        CruxWorld crux = getWorld(world.key());
        if(crux==null) return;
        Chunk chunk = event.getChunk();
        crux.onChunkLoad(chunk);
    }

    @EventHandler(ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        World world = event.getWorld();
        CruxWorld crux = getWorld(world.key());
        if(crux==null) return;
        Chunk chunk = event.getChunk();
        crux.onChunkUnload(chunk);
    }

    @EventHandler(ignoreCancelled = true)
    public void onChunkPopulate(ChunkPopulateEvent event) {
        World world = event.getWorld();
        CruxWorld crux = getWorld(world.key());
        if(crux==null) return;
        Chunk chunk = event.getChunk();
        crux.onChunkPopulate(chunk);
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
        CruxWorld crux = getWorld(world.key());
        if(crux==null) return;
        crux.onLoad();
    }

    @EventHandler(ignoreCancelled = true)
    public void onWorldUnload(WorldUnloadEvent event) {
        World world = event.getWorld();
        CruxWorld crux = getWorld(world.key());
        if(crux==null) return;
        onWorldUnload(crux);
    }

    public void onWorldUnload(CruxWorld crux){
        active.remove(crux.key());
        crux.onUnload(crux.shouldSaveOnNextUnload());
    }

    @EventHandler(ignoreCancelled = true)
    public void onWorldSave(WorldSaveEvent event) {
        World world = event.getWorld();
        CruxWorld crux = getWorld(world.key());
        if(crux==null) return;
        crux.onSave();
    }

    public @NotNull MappedRegistry<Key, CruxWorldCreator> getCreators() {
        return creators;
    }

    public @NotNull ActiveCruxWorldRegistry getActive() {
        return active;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public Server getServer() {
        return server;
    }

    public @NotNull WorldModuleCreatorRegistry getModuleCreators() {
        return moduleCreators;
    }
}
