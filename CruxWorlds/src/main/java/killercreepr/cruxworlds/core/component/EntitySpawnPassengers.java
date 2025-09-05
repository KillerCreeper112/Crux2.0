package killercreepr.cruxworlds.core.component;

import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawn;

import java.util.List;

public class EntitySpawnPassengers {
    public final List<NaturalEntitySpawn> passengers;
    public final boolean append;

    public EntitySpawnPassengers(List<NaturalEntitySpawn> passengers, boolean append) {
        this.passengers = passengers;
        this.append = append;
    }
}
