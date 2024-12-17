package killercreepr.cruxstructures.util;

import killercreepr.crux.api.data.holder.LocationHolder;
import killercreepr.crux.api.data.world.WorldChunkStorage;
import killercreepr.crux.core.util.CruxMap;
import killercreepr.crux.core.util.GetNear;
import killercreepr.cruxstructures.structure.stored.StoredStructure;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetStructureNear extends GetNear<StoredStructure> {
    protected final @NotNull WorldChunkStorage<StoredStructure> storage;

    public GetStructureNear(LocationHolder center, @NotNull WorldChunkStorage<StoredStructure> storage) {
        super(center);
        this.storage = storage;
    }

    public GetStructureNear(@NotNull WorldChunkStorage<StoredStructure> storage) {
        this.storage = storage;
    }

    @Override
    public @NotNull List<StoredStructure> find() {
        Location center = this.center.value();
        Map<StoredStructure, Float> map = new HashMap<>();
        storage.forEach(chunkStorage -> chunkStorage.forEach(stored ->{
            if(filter != null && !filter.test(stored)) return;
            float distance = (float) center.distanceSquared(stored.getPosition().toLocation(center.getWorld()));
            map.put(stored, distance);
        }));

        List<StoredStructure> result;
        switch (operation){
            case NEAREST, FARTHEST ->{
                result = CruxMap.sortMapByFloat(map, operation == Operation.NEAREST);
            }
            default -> result = new ArrayList<>(map.keySet());
        }
        return result;
    }
}
