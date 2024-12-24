package killercreepr.cruxworlds.core.world.manager;

import killercreepr.crux.api.data.tick.Ticked;
import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.crux.api.registry.MappedRegistry;
import killercreepr.crux.core.registry.SimpleMappedRegistry;
import killercreepr.crux.core.util.CruxWorldUtil;
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

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
    protected final @NotNull MappedRegistry<String, CruxWorldCreator> creators = new SimpleMappedRegistry<>();
    protected final @NotNull WorldModuleCreatorRegistry moduleCreators = new WorldModuleCreatorRegistryImpl();
    protected final @NotNull ActiveCruxWorldRegistry active = new ActiveCruxWorldRegistry();

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

    public void tick(){
        active.getTicked().forEach(Ticked::tick);
    }

    @Override
    public @Nullable CruxWorld getOrCreateWorld(@NotNull Key worldType, @NotNull String name) {
        CruxWorld world = getWorld(name);
        if(world != null) return world;
        CruxWorldType type = worldTypes.get(worldType);
        if(type == null) return null;
        return getOrCreateWorld(type, name);
    }

    @Override
    public @Nullable CruxWorld getOrCreateWorld(@NotNull CruxWorldType type, @NotNull String name) {
        CruxWorld world = getWorld(name);
        if(world != null) return world;
        CruxWorld crux = type.generate(name);
        crux.set(CruxWorldsComponents.WORLD_TYPE, type);
        return crux;
    }

    @Override
    public @Nullable CruxWorld getWorld(@NotNull String name) {
        return active.getByName(name);
    }

    @Override
    public @Nullable CruxWorld getWorld(@NotNull UUID uuid) {
        return active.get(uuid);
    }

    @Override
    public CompletableFuture<Boolean> deleteWorld(@NotNull CruxWorld world) {
        unloadWorld(world, false);
        boolean x = CruxWorldUtil.deleteWorld(world.toBukkitWorld());
        if(x) world.onDelete();
        return CompletableFuture.completedFuture(x);
    }

    @Override
    public CompletableFuture<Boolean> deleteWorld(@NotNull String world) {
        CruxWorld crux = getWorld(world);
        if(crux != null) return deleteWorld(crux);
        return CompletableFuture.completedFuture(CruxWorldUtil.deleteWorld(world));
    }

    @Override
    public CompletableFuture<Boolean> unloadWorld(@NotNull CruxWorld world, boolean save) {
        world.setShouldSaveOnNextUnload(save);
        return CompletableFuture.completedFuture(server.unloadWorld(world.toBukkitWorld(), save));
    }

    @Override
    public CompletableFuture<CruxWorld> loadWorld(@NotNull String worldName) {
        CruxWorldUtil.getOrLoadWorld(worldName);
        return CompletableFuture.completedFuture(getWorld(worldName));
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
    public @NotNull KeyedRegistry<CruxWorldType> getWorldTypeRegistry() {
        return worldTypes;
    }

    @Override
    public @NotNull WorldModuleCreatorRegistry getModuleCreatorRegistry() {
        return moduleCreators;
    }

    public @Nullable CruxWorld create(@NotNull World world){
        String name = world.getName();
        CruxWorldCreator creator = creators.get(name);
        Collection<CruxWorldModuleCreator> moduleCreators = this.moduleCreators.get(name);
        if(moduleCreators == null) moduleCreators = new HashSet<>();
        else moduleCreators = new HashSet<>(moduleCreators);

        CruxWorldPreCreateEvent event = new CruxWorldPreCreateEvent(world, moduleCreators);
        if(!event.callEvent()) return null;

        CruxWorld cruxWorld;
        if(creator == null){
            cruxWorld = new SimpleWorld(world, moduleCreators);

        }else cruxWorld = creator.create(world, moduleCreators);
        cruxWorld.onCreate();
        return cruxWorld;
    }

    @EventHandler(ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent event) {
        if(event.isNewChunk()) return;
        World world = event.getWorld();
        CruxWorld crux = getWorld(world.getUID());
        if(crux==null) return;
        Chunk chunk = event.getChunk();
        crux.onChunkLoad(chunk);
    }

    @EventHandler(ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        World world = event.getWorld();
        CruxWorld crux = getWorld(world.getUID());
        if(crux==null) return;
        Chunk chunk = event.getChunk();
        crux.onChunkUnload(chunk);
    }

    @EventHandler(ignoreCancelled = true)
    public void onChunkPopulate(ChunkPopulateEvent event) {
        World world = event.getWorld();
        CruxWorld crux = getWorld(world.getUID());
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
        crux.onUnload(crux.shouldSaveOnNextUnload());
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
