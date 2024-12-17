package killercreepr.cruxworlds.core.registries;

import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.cruxworlds.api.world.entity.KeyedNaturalEntitySpawnGroup;
import killercreepr.cruxworlds.api.world.type.CruxWorldType;

public class WorldsRegistries {
    public static final KeyedRegistry<KeyedNaturalEntitySpawnGroup> NATURAL_ENTITY_SPAWN_GROUP = KeyedRegistry.keyedRegistry();
    public static final KeyedRegistry<CruxWorldType> WORLD_TYPE = KeyedRegistry.keyedRegistry();
}
