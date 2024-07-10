package killercreepr.cruxstructures.manager;

import killercreepr.crux.data.BlockPos;
import killercreepr.crux.data.world.MultiVerseWorldStorage;
import killercreepr.crux.data.world.WorldChunkStorage;
import killercreepr.crux.data.world.standard.MultiVerseBlockPosedStorage;
import killercreepr.cruxconfig.config.bukkit.file.Cfg;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxstructures.file.StorageChunkFile;
import killercreepr.cruxstructures.structure.GenerateResult;
import killercreepr.cruxstructures.structure.active.ActiveStructure;
import killercreepr.cruxstructures.structure.impl.CfgStructureGen;
import killercreepr.cruxstructures.structure.stored.StoredStructure;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class StructureManager implements Listener {
    protected final @NotNull Plugin plugin;
    public StructureManager(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    //world name -> structure gens
    protected final @NotNull Map<String, List<CfgStructureGen>> structures = new HashMap<>();

    protected final @NotNull MultiVerseWorldStorage<StoredStructure> stored = new MultiVerseBlockPosedStorage<>();
    protected final @NotNull MultiVerseWorldStorage<ActiveStructure> active = new MultiVerseBlockPosedStorage<>();

    public void load(){
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
        return new CruxFolder(plugin, "structures");
    }


    public @NotNull CruxFolder createWorldFolder(@NotNull UUID worldUUID){
        return new CruxFolder(plugin, worldUUID.toString());
    }

    public @NotNull StorageChunkFile createChunkFile(@NotNull UUID worldUUID, long chunkKey){
        return new StorageChunkFile(plugin, worldUUID + "/" + chunkKey);
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
        UUID worldUUID = world.getUID();

        WorldChunkStorage<StoredStructure> removed = stored.remove(worldUUID);
        if(removed==null) return;
        removed.getData().forEach((key, value) ->{
            StorageChunkFile file = createChunkFile(worldUUID, key);
            file.structures(value.getData().values());
            if(file.json().isEmpty()){
                file.close();
                file.file().delete();
            }else file.save(true);//todo don't need pretty
        });
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
