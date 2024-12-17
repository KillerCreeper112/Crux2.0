package killercreepr.cruxstructures.manager;

import killercreepr.crux.api.data.tick.Ticked;
import killercreepr.crux.api.data.world.ChunkBlockStorage;
import killercreepr.crux.api.data.world.WorldChunkStorage;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.data.world.WorldBlockPosedStorage;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxstructures.data.world.StoredStructureChunkStorage;
import killercreepr.cruxstructures.file.StorageChunkFile;
import killercreepr.cruxstructures.structure.active.ActiveStructure;
import killercreepr.cruxstructures.structure.generation.StructureGenerator;
import killercreepr.cruxstructures.structure.result.GenerateResult;
import killercreepr.cruxstructures.structure.stored.StoredStructure;
import killercreepr.cruxstructures.structure.stored.TickedStoredStructure;
import killercreepr.cruxworlds.api.world.CruxWorld;
import killercreepr.cruxworlds.core.world.module.SimpleWorldModule;
import org.apache.commons.io.FileUtils;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.logging.Level;
//todo move data into own world module
public class StructureWorldModule extends SimpleWorldModule implements Ticked {
    private static final Predicate<ChunkBlockStorage<StoredStructure>> storedRemoveIf = chunk -> {
        chunk.removeIf(stored -> {
            if(!(stored instanceof TickedStoredStructure a)) return false;
            if (a.shouldStop()) {
                a.stopped();
                return true;
            }
            a.tick();
            return false;
        });
        return chunk.isEmpty();
    };

    private static final Predicate<ChunkBlockStorage<ActiveStructure>> chunkRemoveIf = chunk ->{
        chunk.removeIf(a ->{
            if(a.shouldStop()){
                a.stopped();
                return true;
            }
            a.tick();
            return false;
        });
        return chunk.isEmpty();
    };

    protected final List<StructureGenerator> structureGenerators = new ArrayList<>();
    protected final WorldChunkStorage<StoredStructure> storedStructures = new StoredStructureChunkStorage(new ConcurrentHashMap<>());
    protected final WorldChunkStorage<ActiveStructure> activeStructures = new WorldBlockPosedStorage<>(new ConcurrentHashMap<>());
    //protected boolean dirty = false;

    public StructureWorldModule(@NotNull CruxWorld parent) {
        super(parent);
    }

    public List<StructureGenerator> getStructureGenerators() {
        return structureGenerators;
    }

    public WorldChunkStorage<StoredStructure> getStoredStructures() {
        return storedStructures;
    }

    public WorldChunkStorage<ActiveStructure> getActiveStructures() {
        return activeStructures;
    }

    @Override
    public void tick() {
        activeStructures.removeIf(chunkRemoveIf);
        storedStructures.removeIf(storedRemoveIf);
    }

    public @NotNull CruxFolder createWorldFolder(@NotNull UUID worldUUID){
        return new CruxFolder(Crux.getMainPlugin(), "data/cruxstructures/structures/" + worldUUID);
    }

    public @NotNull StorageChunkFile createChunkFile(@NotNull UUID worldUUID, long chunkKey){
        return new StorageChunkFile(Crux.getMainPlugin(), "data/cruxstructures/structures/" + worldUUID + "/" + chunkKey);
    }

    public void addStoredStructure(StoredStructure stored, long chunkKey){
        storedStructures.add(chunkKey, stored);
        //setDirty();
    }

    protected void addStoredStructureSilently(StoredStructure stored, long chunkKey){
        storedStructures.add(chunkKey, stored);
    }

    public void addStoredStructure(StoredStructure stored, Chunk chunk){
        long chunkKey = chunk.getChunkKey();
        addStoredStructure(stored, chunkKey);
        ActiveStructure active = stored.buildActive(chunk);
        if(active==null) return;
        activeStructures.add(chunkKey, active);
        active.started();
    }

    @Override
    public void onDelete() {
        UUID worldUUID = parent.getUUID();
        CruxFolder folder = createWorldFolder(worldUUID);
        File file = folder.file();
        if(!file.exists() || !file.isDirectory()) return;
        try{
            FileUtils.deleteDirectory(file);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /*public boolean isDirty(){
        return dirty;
    }

    public void setDirty(){
        this.dirty = true;
    }*/

    public <T extends ActiveStructure> @Nullable T getFirstActiveAt(@NotNull Class<T> type, @NotNull Block block){
        return getFirstActiveAt(type, block, null);
    }

    public <T extends ActiveStructure> @Nullable T getFirstActiveAt(@NotNull Class<T> type, @NotNull Block block, @Nullable Predicate<T> filter){
        for(T t : getActiveAt(type, block, filter)){
            return t;
        }
        return null;
    }

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
        activeStructures.forEach(chunkStorage ->{
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
        storedStructures.forEach(chunkStorage ->{
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

    public Collection<StoredStructure> getStored(@Nullable Predicate<StoredStructure> filter){
        return getStored(StoredStructure.class, filter);
    }

    public <T extends StoredStructure> Collection<T> getStored(@NotNull Class<T> type, @Nullable Predicate<T> filter){
        Collection<T> list = new HashSet<>();
        storedStructures.forEach(chunkStorage ->{
            chunkStorage.forEach(s ->{
                if(!type.isAssignableFrom(s.getClass())) return;
                T value = type.cast(s);
                if(filter != null && !filter.test(value)) return;
                list.add(value);
            });
        });
        return list;
    }

    @Override
    public void onChunkLoad(Chunk chunk) {
        long chunkKey = chunk.getChunkKey();
        ChunkBlockStorage<StoredStructure> cached = storedStructures.get(chunkKey);
        if(cached==null) return;
        new HashSet<>(cached.getData().values()).forEach(data ->{
            if(!data.shouldPersist()){
                ActiveStructure removed = activeStructures.remove(chunkKey, data.getPosition());
                if(removed != null) removed.stopped();
                return;
            }
            if(activeStructures.get(chunkKey, data.getPosition()) != null) return;
            ActiveStructure active = data.buildActive(chunk);
            if(active==null) return;
            activeStructures.add(chunkKey, active);
            active.started();
        });
    }

    @Override
    public void onChunkPopulate(Chunk c) {
        List<StructureGenerator> list = structureGenerators;
        Crux.scheduler().runTaskAsync(() ->{
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
        ChunkBlockStorage<ActiveStructure> registered = activeStructures.get(chunkKey); //structureManager.getActive().get(chunk.getWorld().getUID(), chunkKey);
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
                addStoredStructureSilently(v, v.getChunk().getChunkKey());
                //storedStructures.add(v.getChunk().getChunkKey(), v);
                //structureManager.addStoredStructureSilently(v, worldUUID, v.getChunk().getChunkKey());
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
        Crux.log(Level.INFO, "Saving structures in world: " + world.getName());
        UUID worldUUID = world.getUUID();

        CruxFolder folder = createWorldFolder(worldUUID);
        if(folder.file().exists()){
            try{
                FileUtils.deleteDirectory(folder.file());
            }catch (IOException ignored){}
        }
        if(storedStructures.isEmpty()){
            Crux.log(Level.INFO, "No structures saved within world: " + world.getName());
            return;
        }
        AtomicInteger saved = new AtomicInteger();
        storedStructures.getData().forEach((key, value) ->{
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
}
