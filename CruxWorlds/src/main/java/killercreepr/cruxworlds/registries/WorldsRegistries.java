package killercreepr.cruxworlds.registries;

import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.crux.core.registry.SimpleKeyedRegistry;
import killercreepr.cruxworlds.world.entity.KeyedNaturalEntitySpawnGroup;

public class WorldsRegistries {
    public static final KeyedRegistry<KeyedNaturalEntitySpawnGroup> NATURAL_ENTITY_SPAWN_GROUP = new SimpleKeyedRegistry<>();
}
