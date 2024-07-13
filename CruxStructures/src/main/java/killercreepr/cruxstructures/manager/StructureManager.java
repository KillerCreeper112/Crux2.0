package killercreepr.cruxstructures.manager;

import killercreepr.crux.Crux;
import killercreepr.crux.data.BlockPos;
import killercreepr.crux.data.communication.MsgContainer;
import killercreepr.crux.data.world.ChunkBlockStorage;
import killercreepr.crux.data.world.MultiVerseWorldStorage;
import killercreepr.crux.data.world.WorldChunkStorage;
import killercreepr.crux.data.world.standard.MultiVerseBlockPosedStorage;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxstructures.event.StructurePlaceEvent;
import killercreepr.cruxstructures.file.StorageChunkFile;
import killercreepr.cruxstructures.registries.StructureRegistries;
import killercreepr.cruxstructures.structure.GenerateResult;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.active.ActiveStructure;
import killercreepr.cruxstructures.structure.impl.CfgFAWEStructure;
import killercreepr.cruxstructures.structure.impl.CfgStructureGen;
import killercreepr.cruxstructures.structure.stored.StoredStructure;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

public class StructureManager implements Listener {
    protected final @NotNull Plugin plugin;
    public StructureManager(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    //world name -> structure gens
    protected final @NotNull Map<String, List<CfgStructureGen>> structures = new HashMap<>();

    protected final @NotNull MultiVerseWorldStorage<StoredStructure> stored = new MultiVerseBlockPosedStorage<>(new ConcurrentHashMap<>());
    protected final @NotNull MultiVerseWorldStorage<ActiveStructure> active = new MultiVerseBlockPosedStorage<>(new ConcurrentHashMap<>());

    public @NotNull BukkitRunnable buildRunnable(){
        return new BukkitRunnable(){
            @Override
            public void run() {
                active.getData().values().forEach(worldChunk ->{
                    new MsgContainer.Builder().actionBar(
                        worldChunk.getData().size() + ""
                    ).broadcast(true).build().broadcast(null);
                    tick(worldChunk);
                });
            }
        };
    }

    public void tick(@NotNull WorldChunkStorage<ActiveStructure> container){
        new HashSet<>(container.getData().values()).forEach(blockList ->{
            new HashSet<>(blockList.getData().values()).forEach(a ->{
                if(a.shouldStop()){
                    active.remove(a.getCenter().getWorld().getUID(), a.getChunk().getChunkKey(), a.getBlockPos());
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
        File[] files = cfgFolder.file().listFiles();
        new HashSet<>(StructureRegistries.STRUCTURES.values()).forEach(str ->{
            if(str instanceof CfgFAWEStructure){
                StructureRegistries.STRUCTURES.remove(str.key());
            }
        });
        if(files==null) return;

        for(File f : files){
            CruxConfig cfg = new CruxConfig(f);
            CfgFAWEStructure structure = cfg.deserialize(CfgFAWEStructure.class, "");
            if(structure==null) continue;
            StructureRegistries.STRUCTURES.register(structure);
            Crux.log(Level.INFO, "Structure " + structure.key() + ", registered.");
        }
    }

    public void loadGenerationConfiguration(){
        structures.clear();
        CruxFolder cfgFolder = createCfgFolder();
        File[] files = cfgFolder.file().listFiles();
        if(files==null) return;

        for(File f : files){
            CruxConfig cfg = new CruxConfig(f);
            List<String> worlds = cfg.config().getStringList("worlds");
            if(worlds.isEmpty()) continue;

            CfgStructureGen generator = cfg.deserialize(CfgStructureGen.class, "");
            if(generator==null) continue;
            for(String worldName : worlds){
                structures.computeIfAbsent(worldName, e -> new ArrayList<>()).add(generator);
            }

        }
    }

    public void loadConfiguration(){
        loadStructureConfiguration();
        loadGenerationConfiguration();
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
        this.stored.add(spawn.getWorld().getUID(), chunk.getChunkKey(), stored);
        ActiveStructure active = stored.buildActive(chunk);
        if(active==null) return;
        this.active.add(spawn.getWorld().getUID(), chunk.getChunkKey(), active);
        active.started();
    }

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

    public @NotNull CruxFolder createCfgFolder(){
        return new CruxFolder(plugin, "generation");
    }

    public @NotNull CruxFolder createStructuresFolder(){
        return new CruxFolder(plugin, "structures");
    }


    public @NotNull CruxFolder createWorldFolder(@NotNull UUID worldUUID){
        return new CruxFolder(plugin, "saved_structures/" + worldUUID);
    }

    public @NotNull StorageChunkFile createChunkFile(@NotNull UUID worldUUID, long chunkKey){
        return new StorageChunkFile(plugin, "saved_structures/" + worldUUID + "/" + chunkKey);
    }

    @EventHandler(ignoreCancelled = true)
    public void onWorldLoad(WorldLoadEvent event) {
        World world = event.getWorld();
        UUID worldUUID = world.getUID();
        CruxFolder folder = createWorldFolder(worldUUID);
        File[] files = folder.file().listFiles();
        if(files==null) return;

        for(File f : files){
            StorageChunkFile file = new StorageChunkFile(f);
            Map<BlockPos, StoredStructure> values = file.structures();
            file.close();
            values.values().forEach(v ->{
                stored.add(worldUUID, v.getChunk().getChunkKey(), v);
            });
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onWorldUnload(WorldUnloadEvent event) {
        World world = event.getWorld();
        saveWorld(world);
    }

    public void saveAllWorlds(){
        for(World world : Bukkit.getWorlds()){
            saveWorld(world);
        }
    }

    public void saveWorld(@NotNull World world){
        Crux.log(Level.INFO, "World " + world.getName() + " has been saving now.");
        UUID worldUUID = world.getUID();
        WorldChunkStorage<StoredStructure> removed = stored.remove(worldUUID);

        CruxFolder folder = createWorldFolder(worldUUID);
        if(folder.file().exists()){
            try{
                FileUtils.deleteDirectory(folder.file());
            }catch (IOException ignored){}
        }
        if(removed==null) return;
        removed.getData().forEach((key, value) ->{
            StorageChunkFile file = createChunkFile(worldUUID, key);
            file.structures(value.getData().values());
            Crux.log(Level.INFO, "WORLD CHUNK! ");
            if(file.json().isEmpty()){
                file.close();
                file.file().delete();
                Crux.log(Level.INFO, "DELETED");
            }else{
                file.save(true);//todo don't need pretty
                Crux.log(Level.INFO, "SAVED BOI");
            }
        });

    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();
        long chunkKey = chunk.getChunkKey();
        ChunkBlockStorage<StoredStructure> cached = stored.get(chunk.getWorld().getUID(), chunkKey);
        if(cached==null) return;
        new HashSet<>(cached.getData().values()).forEach(data ->{
            if(!data.shouldPersist()){
                ActiveStructure removed = active.remove(chunk.getWorld().getUID(), chunkKey, data.getBlockPos());
                if(removed != null) removed.stopped();
                return;
            }
            //BlockPos blockPos = data.getBlockPos();
            //todo maybe if(cached.get(blockPos) != null) return;
            ActiveStructure active = data.buildActive(chunk);
            if(active==null) return;
            this.active.add(chunk.getWorld().getUID(), chunkKey, active);
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

    /*@EventHandler(ignoreCancelled = true)
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
    }*/

    public @NotNull Plugin getPlugin() {
        return plugin;
    }

    public @NotNull Map<String, List<CfgStructureGen>> getStructures() {
        return structures;
    }

    public @NotNull MultiVerseWorldStorage<StoredStructure> getStored() {
        return stored;
    }

    public @NotNull MultiVerseWorldStorage<ActiveStructure> getActive() {
        return active;
    }
}
