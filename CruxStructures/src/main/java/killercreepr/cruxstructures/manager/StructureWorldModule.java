package killercreepr.cruxstructures.manager;

import killercreepr.crux.api.data.world.ChunkBlockStorage;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxstructures.file.StorageChunkFile;
import killercreepr.cruxstructures.structure.active.ActiveStructure;
import killercreepr.cruxstructures.structure.generation.StructureGenerator;
import killercreepr.cruxstructures.structure.result.GenerateResult;
import killercreepr.cruxstructures.structure.stored.StoredStructure;
import killercreepr.cruxworlds.api.world.CruxWorld;
import killercreepr.cruxworlds.core.world.module.SimpleWorldModule;
import org.apache.commons.io.FileUtils;
import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
//todo move data into own world module
public class StructureWorldModule extends SimpleWorldModule {
    protected final StructureManager structureManager;

    public StructureWorldModule(@NotNull CruxWorld parent, StructureManager structureManager) {
        super(parent);
        this.structureManager = structureManager;
    }

    @Override
    public void onDelete() {
        UUID worldUUID = parent.getUUID();
        CruxFolder folder = structureManager.createWorldFolder(worldUUID);
        File file = folder.file();
        if(!file.exists() || !file.isDirectory()) return;
        try{
            FileUtils.deleteDirectory(file);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onChunkLoad(Chunk chunk) {
        long chunkKey = chunk.getChunkKey();
        UUID worldUUID = chunk.getWorld().getUID();
        ChunkBlockStorage<StoredStructure> cached = structureManager.getStored().get(worldUUID, chunkKey);
        if(cached==null) return;
        new HashSet<>(cached.getData().values()).forEach(data ->{
            if(!data.shouldPersist()){
                ActiveStructure removed = structureManager.getActive().remove(worldUUID, chunkKey, data.getPosition());
                if(removed != null) removed.stopped();
                return;
            }
            if(structureManager.getActive().get(worldUUID, chunkKey, data.getPosition()) != null) return;
            ActiveStructure active = data.buildActive(chunk);
            if(active==null) return;
            this.structureManager.getActive().add(worldUUID, chunkKey, active);
            active.started();
        });
    }

    @Override
    public void onChunkPopulate(Chunk c) {
        List<StructureGenerator> list = structureManager.getStructures().get(c.getWorld().getName());
        if(list==null) return;
        structureManager.getPlugin().getServer().getScheduler().runTaskAsynchronously(structureManager.getPlugin(), task ->{
            List<StructureGenerator> newList = new ArrayList<>(list);
            Collections.shuffle(newList);
            for(StructureGenerator gen : newList){
                GenerateResult result = gen.generate(c);
                if(result.getPlaceEvent() == null || result.getPlaceEvent().isCancelled()) continue;
                break;
            }
        });
    }

    @Override
    public void onChunkUnload(Chunk chunk) {
        long chunkKey = chunk.getChunkKey();
        ChunkBlockStorage<ActiveStructure> registered = structureManager.getActive().get(chunk.getWorld().getUID(), chunkKey);
        if(registered==null) return;
        new HashSet<>(registered.getData().values()).forEach(block ->{
            registered.remove(block);
            block.stopped();
            //todo maybe addCache(block);
        });
    }

    @Override
    public void onLoad() {
        super.onLoad();
        CruxWorld world = parent;
        UUID worldUUID = world.getUUID();
        CruxFolder folder = structureManager.createWorldFolder(worldUUID);
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
                structureManager.addStoredStructureSilently(v, worldUUID, v.getChunk().getChunkKey());
                loaded++;
            }
        }
        Crux.log(Level.INFO, "Loaded " + loaded + " structures in world, " + world.getName());
    }

    @Override
    public void onUnload(boolean save) {
        super.onUnload(save);
        if(!save) return;
        CruxWorld world = parent;
        UUID worldUUID = parent.getUUID();
        CruxFolder folder = structureManager.createWorldFolder(worldUUID);
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
                structureManager.addStoredStructureSilently(v, worldUUID, v.getChunk().getChunkKey());
                loaded++;
            }
        }
        Crux.log(Level.INFO, "Loaded " + loaded + " structures in world, " + world.getName());
    }
}
