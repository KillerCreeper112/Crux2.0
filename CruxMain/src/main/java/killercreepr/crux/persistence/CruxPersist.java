package killercreepr.crux.persistence;

import killercreepr.crux.item.component.ToolComponent;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class CruxPersist {
    public static final PersistTag<Boolean> IGNORED_MOB_TARGET = PersistTag.register(new PersistTag<>(PersistentDataType.BOOLEAN, "ignored_mob_target"));
    public static final PersistTag<String> SPAWN_REASON = PersistTag.register(new PersistTag<>(PersistentDataType.STRING, "spawn_reason"));
    public static final PersistTag<UUID> OWNER = PersistTag.register(new PersistTag<>(CruxPersistence.UUID, "owner"));
    public static final PersistTag<ToolComponent> TOOL = PersistTag.register(new PersistTag<>(CruxPersistence.TOOL_COMPONENT, "tool"));
}
