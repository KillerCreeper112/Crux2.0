package killercreepr.cruxentities.registries;

import killercreepr.crux.registry.KeyedRegistry;
import killercreepr.crux.registry.SimpleKeyedRegistry;
import killercreepr.cruxentities.entity.CruxMob;
import killercreepr.cruxentities.entity.MobCategory;

public class CruxEntityRegistries {
    public static final KeyedRegistry<CruxMob> ENTITIES = new SimpleKeyedRegistry<>();
    public static final KeyedRegistry<MobCategory> MOB_CATEGORY = new SimpleKeyedRegistry<>();
}
