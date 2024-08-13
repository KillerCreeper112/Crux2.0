package killercreepr.cruxstructures.util;

import killercreepr.crux.data.world.MultiVerseWorldStorage;
import killercreepr.crux.data.world.WorldChunkStorage;
import killercreepr.crux.location.LocationHolder;
import killercreepr.crux.util.CruxMap;
import killercreepr.crux.util.GetNear;
import killercreepr.cruxstructures.structure.stored.StoredStructure;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetStructureNear extends GetNear<StoredStructure> {
    protected final @NotNull MultiVerseWorldStorage<StoredStructure> storage;

    public GetStructureNear(LocationHolder center, @NotNull MultiVerseWorldStorage<StoredStructure> storage) {
        super(center);
        this.storage = storage;
    }

    public GetStructureNear(@NotNull MultiVerseWorldStorage<StoredStructure> storage) {
        this.storage = storage;
    }

    @Override
    public @NotNull List<StoredStructure> find() {
        Location center = this.center.value();
        Map<StoredStructure, Float> map = new HashMap<>();
        WorldChunkStorage<StoredStructure> worldStorage = storage.get(center.getWorld().getUID());
        if(worldStorage==null) return new ArrayList<>(map.keySet());
        worldStorage.forEach(chunkStorage -> chunkStorage.forEach(stored ->{
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
