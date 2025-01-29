package killercreepr.cruxstructures.api.structure;

import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.data.world.StoredChunk;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxstructures.api.component.StructureComponent;
import killercreepr.cruxstructures.api.component.StructureEditorComponent;
import killercreepr.cruxstructures.api.event.StructurePlaceEvent;
import killercreepr.cruxstructures.core.structure.stored.SimpleStoredStructure;
import net.kyori.adventure.key.Keyed;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public interface Structure extends Keyed, DataComponentHandler {
    @NotNull
    default StructurePlaceEvent place(@NotNull Location at){
        return place(at, (CruxMath.random().nextInt(4) * 90));
    }

    @NotNull
    StructurePlaceEvent place(@NotNull Location at, double rotation);
    @NotNull
    BoundingBox boundingBox();
    @NotNull
    CruxPosition originPos();

    default boolean isPersistent(){ return false; }

    default @Nullable StoredStructure buildStored(@NotNull StoredChunk chunk, @NotNull CruxPosition center, double rotation){
        Collection<StructureComponent> structureComponents = new HashSet<>();
        AtomicReference<StoredStructure> storedBuilt = new AtomicReference<>(
            new SimpleStoredStructure(this, chunk, center, rotation)
        );
        forEach(typed ->{
            if(typed.getValue() instanceof StructureComponent s) structureComponents.add(s);
            if(typed.getValue() instanceof StructureEditorComponent c){
                storedBuilt.set(c.onCreated(chunk, center, rotation, this, storedBuilt.get()));
            }
        });

        StoredStructure stored = storedBuilt.get();
        if(stored == null) return null;

        structureComponents.forEach(component ->{
            component.onCreated(chunk, center, rotation, stored);
        });
        return stored;
    }

    default @Nullable StoredStructure buildStored(@NotNull Location center, double rotation){
        Collection<StructureComponent> structureComponents = new HashSet<>();
        AtomicReference<StoredStructure> storedBuilt = new AtomicReference<>(
            new SimpleStoredStructure(this, StoredChunk.from(center), CruxPosition.block(center), rotation)
        );
        forEach(typed ->{
            if(typed.getValue() instanceof StructureComponent s) structureComponents.add(s);
            if(typed.getValue() instanceof StructureEditorComponent c){
                storedBuilt.set(c.onCreated(center, rotation, this, storedBuilt.get()));
            }
        });

        StoredStructure stored = storedBuilt.get();
        if(stored == null) return null;

        StoredChunk chunk = StoredChunk.from(center);
        CruxPosition pos = CruxPosition.block(center);

        structureComponents.forEach(component ->{
            component.onCreated(chunk, pos, rotation, stored);
        });
        return stored;
    }

    default @NotNull Collection<CruxPosition> getBlocks(double rotation){
        return getBlocks(rotation, null);
    }
    @NotNull Collection<CruxPosition> getBlocks(double rotation, @Nullable Predicate<CruxPosition> filter);
    @ApiStatus.Experimental
    @NotNull Map<CruxPosition, BlockData> getBlockMap(double rotation);
}
