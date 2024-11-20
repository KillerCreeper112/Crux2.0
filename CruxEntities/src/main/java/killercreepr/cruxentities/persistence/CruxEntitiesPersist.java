package killercreepr.cruxentities.persistence;

import killercreepr.crux.core.persistence.CruxPersistence;
import killercreepr.crux.core.persistence.PersistTag;
import net.kyori.adventure.key.Key;

public class CruxEntitiesPersist {
    public static void register(){}

    public static final PersistTag<Key> ENTITY = PersistTag.register(new PersistTag<>(CruxPersistence.CRUX_KEY, "entity"));
}
