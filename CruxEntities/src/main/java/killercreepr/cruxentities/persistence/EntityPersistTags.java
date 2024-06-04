package killercreepr.cruxentities.persistence;

import killercreepr.crux.persistence.CruxPersistence;
import killercreepr.crux.persistence.PersistTag;
import net.kyori.adventure.key.Key;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;

public class EntityPersistTags {
    public static void register(){}

    public static final PersistTag<Key> ENTITY =
            PersistTag.register(new PersistTag<>(CruxPersistence.CRUX_KEY, "entity"));
}
