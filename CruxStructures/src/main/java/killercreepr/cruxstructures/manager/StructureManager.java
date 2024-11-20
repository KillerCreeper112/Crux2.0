package killercreepr.cruxstructures.manager;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.core.Crux;
import killercreepr.crux.api.data.tick.ManagedTicked;
import killercreepr.crux.api.data.world.ChunkBlockStorage;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.api.data.world.MultiVerseWorldStorage;
import killercreepr.crux.api.data.world.WorldChunkStorage;
import killercreepr.crux.core.data.world.MultiVerseBlockPosedStorage;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxstructures.CruxStructuresModule;
import killercreepr.cruxstructures.config.loader.StructureGeneratorLoader;
import killercreepr.cruxstructures.config.loader.StructureLoader;
import killercreepr.cruxstructures.event.StructurePlaceEvent;
import killercreepr.cruxstructures.file.StorageChunkFile;
import killercreepr.cruxstructures.registries.StructureRegistries;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.active.ActiveStructure;
import killercreepr.cruxstructures.structure.generation.StructureGenerator;
import killercreepr.cruxstructures.structure.impl.CfgFAWEStructure;
import killercreepr.cruxstructures.structure.result.GenerateResult;
import killercreepr.cruxstructures.structure.stored.StoredStructure;
import killercreepr.cruxstructures.structure.stored.TickedStoredStructure;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;

public class StructureManager implements Listener {
    protected final @NotNull Plugin plugin;
    public StructureManager(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    //world name -> structure gens
    protected final @NotNull Map<String, List<StructureGenerator>> structures = new HashMap<>();

    protected final @NotNull MultiVerseWorldStorage<StoredStructure> stored = new MultiVerseBlockPosedStorage<>(new ConcurrentHashMap<>());
    protected final @NotNull MultiVerseWorldStorage<ActiveStructure> active = new MultiVerseBlockPosedStorage<>(new ConcurrentHashMap<>());
    protected final @NotNull MultiVerseWorldStorage<TickedStoredStructure> storedTicked = new MultiVerseBlockPosedStorage<>(new ConcurrentHashMap<>());

    public @NotNull Collection<StoredStructure> getStoredAt(@NotNull Block block){
        return getStoredAt(block, null);
    }

    public @NotNull Collection<StoredStructure> getStoredAt(@NotNull Block block, @Nullable Predicate<StoredStructure> filter){
        return getStoredAt(StoredStructure.class, block, filter);
    }

    public <T extends StoredStructure> @NotNull Collection<T> getStoredAt(@NotNull Class<T> type, @NotNull Block block){
        return getStoredAt(type, block, null);
    }

    public <T extends StoredStructure> @NotNull Collection<T> getStoredAt(@NotNull Class<T> type, @NotNull Block block, @Nullable Predicate<T> filter){
        Collection<T> list = new HashSet<>();
        WorldChunkStorage<StoredStructure> worldStorage = stored.get(block.getWorld().getUID());
        if(worldStorage ==null) return list;

        worldStorage.forEach(chunkStorage ->{
            for(StoredStructure s : chunkStorage){
                if(!s.getBoundingBox().contains(block.getLocation().toVector())) continue;
                if(!type.isAssignableFrom(s.getClass())) continue;
                T value = type.cast(s);
                if(filter != null && !filter.test(value)) continue;
                list.add(value);
            }
        });
        return list;
    }

    public <T extends StoredStructure> @Nullable T getFirstStoredAt(@NotNull Class<T> type, @NotNull Block block){
        return getFirstStoredAt(type, block, null);
    }

    public <T extends StoredStructure> @Nullable T getFirstStoredAt(@NotNull Class<T> type, @NotNull Block block, @Nullable Predicate<T> filter){
        for(T t : getStoredAt(type, block, filter)){
            return t;
        }
        return null;
    }

    public Collection<StoredStructure> getStored(@NotNull UUID world, @Nullable Predicate<StoredStructure> filter){
        return getStored(world, StoredStructure.class, filter);
    }

    public <T extends StoredStructure> Collection<T> getStored(@NotNull UUID world, @NotNull Class<T> type, @Nullable Predicate<T> filter){
        Collection<T> list = new HashSet<>();
        WorldChunkStorage<StoredStructure> worldStorage = stored.get(world);
        if(worldStorage ==null) return list;
        worldStorage.forEach(chunkStorage ->{
            chunkStorage.forEach(s ->{
                if(!type.isAssignableFrom(s.getClass())) return;
                T value = type.cast(s);
                if(filter != null && !filter.test(value)) return;
                list.add(value);
            });
        });
        return list;
    }

    //active
    public @NotNull Collection<ActiveStructure> getActiveAt(@NotNull Block block){
        return getActiveAt(block, null);
    }

    public @NotNull Collection<ActiveStructure> getActiveAt(@NotNull Block block, @Nullable Predicate<ActiveStructure> filter){
        return getActiveAt(ActiveStructure.class, block, filter);
    }

    public <T extends ActiveStructure> @NotNull Collection<T> getActiveAt(@NotNull Class<T> type, @NotNull Block block){
        return getActiveAt(type, block, null);
    }

    public <T extends ActiveStructure> @NotNull Collection<T> getActiveAt(@NotNull Class<T> type, @NotNull Block block, @Nullable Predicate<T> filter){
        Collection<T> list = new HashSet<>();
        WorldChunkStorage<ActiveStructure> worldStorage = active.get(block.getWorld().getUID());
        if(worldStorage ==null) return list;
        worldStorage.forEach(chunkStorage ->{
            for(ActiveStructure s : chunkStorage){
                if(!s.getData().getBoundingBox().contains(block.getLocation().toVector())) continue;
                if(!type.isAssignableFrom(s.getClass())) continue;
                T value = type.cast(s);
                if(filter != null && !filter.test(value)) continue;
                list.add(value);
            }
        });
        return list;
    }

    public <T extends ActiveStructure> @Nullable T getFirstActiveAt(@NotNull Class<T> type, @NotNull Block block){
        return getFirstActiveAt(type, block, null);
    }

    public <T extends ActiveStructure> @Nullable T getFirstActiveAt(@NotNull Class<T> type, @NotNull Block block, @Nullable Predicate<T> filter){
        for(T t : getActiveAt(type, block, filter)){
            return t;
        }
        return null;
    }

    private final Predicate<ChunkBlockStorage<? extends ManagedTicked>> chunkRemoveIf = chunk ->{
        chunk.getData().values().removeIf(a ->{
            if(a.shouldStop()){
                a.stopped();
                return true;
            }
            a.tick();
            return false;
        });
        return chunk.isEmpty();
    };
    private final Consumer<WorldChunkStorage<? extends ManagedTicked>> worldForEach = world ->{
        world.getData().values().removeIf(chunkRemoveIf);
    };
    public @NotNull BukkitRunnable buildRunnable(){
        return new BukkitRunnable(){
            @Override
            public void run() {
                active.getData().values().forEach(worldForEach);
                storedTicked.getData().values().forEach(worldForEach);
                //active.getData().values().forEach(worldChunk -> tick(worldChunk));
            }
        };
    }

    public void tick(@NotNull WorldChunkStorage<ActiveStructure> container){
        new HashSet<>(container.getData().values()).forEach(blockList ->{
            new HashSet<>(blockList.getData().values()).forEach(a ->{
                if(a.shouldStop()){
                    active.remove(a.getCenter().getWorld().getUID(), a.getChunk().getChunkKey(), a.getPosition());
                    a.stopped();
                    return;
                }
                a.tick();
            });
        });
    }

    public void tick(@NotNull World world){
        WorldChunkStorage<ActiveStructure> container = active.get(world.getUID());
        if(container==null) return;
        tick(container);
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

    public void loadGenerationConfiguration(){
        structures.clear();
        CruxFolder cfgFolder = createCfgFolder();

        new StructureGeneratorLoader((cfg, generator) ->{
            List<String> worlds = cfg.deserialize("worlds",
                new TypeToken<List<String>>(){}.getType());
            if(worlds == null || worlds.isEmpty()){
                Crux.log(Level.WARNING, "Structure generator, " + cfg.file().getName() + " does not have any worlds set for it.");
                return;
            }
            for(String worldName : worlds){
                structures.computeIfAbsent(worldName, e -> new ArrayList<>()).add(generator);
            }
            Crux.log(Level.INFO, "Registered structure generator: " + cfg.file().getName() + " for worlds: " + worlds);
        }).loadConfiguration(cfgFolder.file());
    }

    public void loadConfiguration(){
        loadStructureConfiguration();
        loadGenerationConfiguration();
    }

    public void addStoredStructure(StoredStructure stored, UUID world, long chunkKey){
        this.stored.add(world, chunkKey, stored);
        if(stored instanceof TickedStoredStructure ticked){
            this.storedTicked.add(world, chunkKey, ticked);
        }
    }

    protected void addStoredStructureSilently(StoredStructure stored, UUID world, long chunkKey){
        this.stored.addSilently(world, chunkKey, stored);
        if(stored instanceof TickedStoredStructure ticked){
            this.storedTicked.addSilently(world, chunkKey, ticked);
        }
    }

    public void addStoredStructure(StoredStructure stored, UUID world, Chunk chunk){
        long chunkKey = chunk.getChunkKey();
        addStoredStructure(stored, world, chunkKey);
        ActiveStructure active = stored.buildActive(chunk);
        if(active==null) return;
        this.active.add(world, chunkKey, active);
        active.started();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onStructurePlace(StructurePlaceEvent event) {
        Structure structure = event.getStructure();
        if(!structure.isPersistent()) return;
        Location spawn = event.getLocation();
        StoredStructure stored = structure.buildStored(spawn, event.getRotation());
        if(stored==null) return;
        if(!stored.shouldPersist()) return;
        Chunk chunk = spawn.getChunk();
        addStoredStructure(stored, spawn.getWorld().getUID(), chunk);
    }

    @EventHandler(ignoreCancelled = true)
    public void onChunkPopulate(ChunkPopulateEvent event) {
        Chunk c = event.getChunk();
        List<StructureGenerator> list = structures.get(c.getWorld().getName());
        if(list==null) return;
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, task ->{
            List<StructureGenerator> newList = new ArrayList<>(list);
            Collections.shuffle(newList);
            for(StructureGenerator gen : newList){
                GenerateResult result = gen.generate(c);
                if(result.getPlaceEvent() == null || result.getPlaceEvent().isCancelled()) continue;
                break;
            }
        });
    }

    public @NotNull CruxFolder createCfgFolder(){
        return new CruxFolder(plugin, "generation");
    }

    public @NotNull CruxFolder createStructuresFolder(){
        return new CruxFolder(plugin, "structures");
    }


    public @NotNull CruxFolder createWorldFolder(@NotNull UUID worldUUID){
        return new CruxFolder(plugin, "data/cruxstructures/structures/" + worldUUID);
    }

    public @NotNull StorageChunkFile createChunkFile(@NotNull UUID worldUUID, long chunkKey){
        return new StorageChunkFile(plugin, "data/cruxstructures/structures/" + worldUUID + "/" + chunkKey);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onWorldLoad(WorldLoadEvent event) {
        World world = event.getWorld();
        onWorldLoaded(world);
    }

    public void onWorldLoaded(World world){
        UUID worldUUID = world.getUID();
        CruxFolder folder = createWorldFolder(worldUUID);
        File[] files = folder.file().listFiles();
        Crux.log(Level.INFO, "Loading structures in world, " + world.getName());
        if(files==null){
            Crux.log(Level.INFO, "No structures loaded in world, " + world.getName());
            return;
        }

        int loaded = 0;
        for(File f : files){
            StorageChunkFile file = new StorageChunkFile(f);
            Map<CruxPosition, StoredStructure> values = file.structures();
            file.close();
            for (StoredStructure v : values.values()) {
                addStoredStructureSilently(v, worldUUID, v.getChunk().getChunkKey());
                loaded++;
            }
        }
        Crux.log(Level.INFO, "Loaded " + loaded + " structures in world, " + world.getName());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onWorldUnload(WorldUnloadEvent event) {
        World world = event.getWorld();
        onWorldUnloaded(world);
    }

    public void onWorldUnloaded(World world){
        saveWorld(world);
    }

    public void saveAllWorlds(){
        for(World world : Bukkit.getWorlds()){
            saveWorld(world);
        }
    }

    public void saveWorld(@NotNull World world){
        /*if(!stored.isDirty()){
            Crux.log(Level.INFO, world.getName() + " is skipping structure saving because no new changes were made.");
            return;
        }*/
        Crux.log(Level.INFO, "Saving structures in world: " + world.getName());
        UUID worldUUID = world.getUID();
        WorldChunkStorage<StoredStructure> removed = stored.remove(worldUUID);
        storedTicked.remove(worldUUID);

        CruxFolder folder = createWorldFolder(worldUUID);
        if(folder.file().exists()){
            try{
                FileUtils.deleteDirectory(folder.file());
            }catch (IOException ignored){}
        }
        if(removed==null){
            Crux.log(Level.INFO, "No structures saved within world: " + world.getName());
            return;
        }
        AtomicInteger saved = new AtomicInteger();
        removed.getData().forEach((key, value) ->{
            StorageChunkFile file = createChunkFile(worldUUID, key);
            file.reloadIfNeeded();
            file.structures(value.getData().values());
            saved.addAndGet(value.getData().size());
            if(file.json().isEmpty()){
                file.close();
                file.file().delete();
            }else{
                file.save(false);
            }
        });
        Crux.log(Level.INFO, "Saved " + saved.get() + " structures in world, " + world.getName());

    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();
        long chunkKey = chunk.getChunkKey();
        UUID worldUUID = chunk.getWorld().getUID();
        ChunkBlockStorage<StoredStructure> cached = stored.get(worldUUID, chunkKey);
        if(cached==null) return;
        new HashSet<>(cached.getData().values()).forEach(data ->{
            if(!data.shouldPersist()){
                ActiveStructure removed = active.remove(worldUUID, chunkKey, data.getPosition());
                if(removed != null) removed.stopped();
                return;
            }
            if(active.get(worldUUID, chunkKey, data.getPosition()) != null) return;
            ActiveStructure active = data.buildActive(chunk);
            if(active==null) return;
            this.active.add(worldUUID, chunkKey, active);
            active.started();
        });
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        Chunk chunk = event.getChunk();
        long chunkKey = chunk.getChunkKey();
        ChunkBlockStorage<ActiveStructure> registered = active.get(chunk.getWorld().getUID(), chunkKey);
        if(registered==null) return;
        new HashSet<>(registered.getData().values()).forEach(block ->{
            registered.remove(block);
            block.stopped();
            //todo maybe addCache(block);
        });
    }

    public @NotNull Plugin getPlugin() {
        return plugin;
    }

    public @NotNull Map<String, List<StructureGenerator>> getStructures() {
        return structures;
    }

    public @NotNull MultiVerseWorldStorage<StoredStructure> getStored() {
        return stored;
    }

    public @NotNull MultiVerseWorldStorage<ActiveStructure> getActive() {
        return active;
    }

    public @NotNull MultiVerseWorldStorage<TickedStoredStructure> getStoredTicked() {
        return storedTicked;
    }
}
