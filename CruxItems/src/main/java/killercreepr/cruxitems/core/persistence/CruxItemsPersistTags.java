package killercreepr.cruxitems.core.persistence;

import killercreepr.crux.core.persistence.CruxPersistence;
import killercreepr.crux.core.persistence.PersistTag;
import net.kyori.adventure.key.Key;

public class CruxItemsPersistTags {
    public static final PersistTag<Key> ITEM = new PersistTag<>(CruxPersistence.CRUX_KEY, "item");
}
