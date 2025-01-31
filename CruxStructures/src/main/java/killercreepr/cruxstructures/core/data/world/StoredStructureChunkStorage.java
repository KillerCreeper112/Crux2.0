package killercreepr.cruxstructures.core.data.world;

import killercreepr.crux.api.data.world.ChunkBlockStorage;
import killercreepr.crux.api.data.world.WorldChunkStorage;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.data.world.WorldBlockPosedStorage;
import killercreepr.cruxstructures.api.structure.StoredStructure;
import killercreepr.cruxstructures.api.structure.TickedStoredStructure;
import killercreepr.cruxstructures.api.world.module.StructureWorldModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.logging.Level;

public class StoredStructureChunkStorage extends WorldBlockPosedStorage<StoredStructure> {
    protected final WorldChunkStorage<TickedStoredStructure> tickedStored = new WorldBlockPosedStorage<>(new ConcurrentHashMap<>());
    protected final StructureWorldModule module;
    public StoredStructureChunkStorage(@NotNull Map<Long, ChunkBlockStorage<StoredStructure>> data, StructureWorldModule module) {
        super(data);
        this.module = module;
    }

    public WorldChunkStorage<TickedStoredStructure> getTickedStored() {
        return tickedStored;
    }

    public boolean tickTickeds(){
        return tickedStored.removeIf(chunk -> {
            chunk.removeIf(a -> {
                if (a.shouldStop(module)) {
                    a.stopped(module);
                    super.remove(a.getChunk().getChunkKey(), a.getPosition());
                    return true;
                }
                a.tick(module);
                return false;
            });
            return chunk.isEmpty();
        });
    }

    @Override
    public ChunkBlockStorage<StoredStructure> remove(long chunkKey) {
        tickedStored.remove(chunkKey);
        return super.remove(chunkKey);
    }

    @Override
    public StoredStructure remove(long chunkKey, @NotNull CruxPosition pos) {
        tickedStored.remove(chunkKey, pos);
        return super.remove(chunkKey, pos);
    }

    @Override
    public boolean removeIf(Predicate<ChunkBlockStorage<StoredStructure>> predicate) {
        tickedStored.removeIf(ticked -> predicate.test((ChunkBlockStorage<StoredStructure>) (ChunkBlockStorage) ticked));
        return super.removeIf(predicate);
    }

    @Override
    public StoredStructure add(long chunkKey, @NotNull StoredStructure block) {
        Crux.log(Level.WARNING, "added=" + block + ", " + (block instanceof TickedStoredStructure s));
        if(block instanceof TickedStoredStructure s){
            tickedStored.add(chunkKey, s);
            s.started(module);
        }
        return super.add(chunkKey, block);
    }

    @Override
    public ChunkBlockStorage<StoredStructure> set(long chunkKey, @Nullable ChunkBlockStorage<StoredStructure> container) {
        tickedStored.set(chunkKey, null);
        if(container != null){
            container.forEach(s ->{
                if(!(s instanceof TickedStoredStructure d)) return;
                tickedStored.add(chunkKey, d);
            });
        }
        return super.set(chunkKey, container);
    }
}
