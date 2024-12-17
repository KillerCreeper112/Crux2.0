package killercreepr.cruxworlds.registries;

import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.cruxworlds.world.creator.CruxWorldType;
import killercreepr.cruxworlds.world.entity.KeyedNaturalEntitySpawnGroup;

public class WorldsRegistries {
    public static final KeyedRegistry<KeyedNaturalEntitySpawnGroup> NATURAL_ENTITY_SPAWN_GROUP = KeyedRegistry.keyedRegistry();
    public static final KeyedRegistry<CruxWorldType> WORLD_TYPE = KeyedRegistry.keyedRegistry();
}
