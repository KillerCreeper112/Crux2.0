package killercreepr.cruxentities.persistence;

import killercreepr.crux.persistence.CruxPersistence;
import killercreepr.crux.persistence.PersistTag;
import net.kyori.adventure.key.Key;
import org.bukkit.persistence.PersistentDataType;

public class CruxEntitiesPersist {
    public static void register(){}

    public static final PersistTag<Key> ENTITY = PersistTag.register(new PersistTag<>(CruxPersistence.CRUX_KEY, "entity"));
    public static final PersistTag<String> SPAWN_REASON = PersistTag.register(new PersistTag<>(PersistentDataType.STRING, "spawn_reason"));
}
