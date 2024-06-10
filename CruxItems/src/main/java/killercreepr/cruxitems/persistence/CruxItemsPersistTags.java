package killercreepr.cruxitems.persistence;

import killercreepr.crux.persistence.CruxPersistence;
import killercreepr.crux.persistence.PersistTag;
import net.kyori.adventure.key.Key;

public class CruxItemsPersistTags {
    public static final PersistTag<Key> ITEM = new PersistTag<>(CruxPersistence.CRUX_KEY, "item");
}
