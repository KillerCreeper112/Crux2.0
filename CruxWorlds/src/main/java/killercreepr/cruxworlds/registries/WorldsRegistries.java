package killercreepr.cruxworlds.registries;

import killercreepr.crux.registry.KeyedRegistry;
import killercreepr.crux.registry.SimpleKeyedRegistry;
import killercreepr.cruxworlds.world.entity.KeyedNaturalEntitySpawnGroup;

public class WorldsRegistries {
    public static final KeyedRegistry<KeyedNaturalEntitySpawnGroup> NATURAL_ENTITY_SPAWN_GROUP = new SimpleKeyedRegistry<>();
}
