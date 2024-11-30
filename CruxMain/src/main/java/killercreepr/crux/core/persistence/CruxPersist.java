package killercreepr.crux.core.persistence;

import killercreepr.crux.api.loot.item.ItemLootTable;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

public class CruxPersist {
    public static final PersistTag<Boolean> IGNORED_MOB_TARGET = PersistTag.register(new PersistTag<>(PersistentDataType.BOOLEAN, "ignored_mob_target"));
    public static final PersistTag<Long> LOOT_GENERATED_TIME = PersistTag.register(new PersistTag<>(PersistentDataType.LONG, "loot_generated_time"));
    public static final PersistTag<ItemLootTable> ITEM_LOOT_TABLE = PersistTag.register(new PersistTag<>(CruxPersistence.ITEM_LOOT_TABLE, "item_loot_table"));
    public static final PersistTag<List<ItemLootTable>> ITEM_LOOT_TABLES = PersistTag.register(new PersistTag<>(CruxPersistence.LIST.ITEM_LOOT_TABLE, "item_loot_tables"));
    public static final PersistTag<String> SPAWN_REASON = PersistTag.register(new PersistTag<>(PersistentDataType.STRING, "spawn_reason"));
    public static final PersistTag<UUID> OWNER = PersistTag.register(new PersistTag<>(CruxPersistence.UUID, "owner"));
    public static final PersistTag<Integer> RICOCHET_ENTITY = PersistTag.register(new PersistTag<>(PersistentDataType.INTEGER, "ricochet_entity"));
    public static final PersistTag<Integer> RICOCHET_BLOCK = PersistTag.register(new PersistTag<>(PersistentDataType.INTEGER, "ricochet_block"));
    public static final PersistTag<PersistentDataContainer> COMPONENTS = PersistTag.register(new PersistTag<>(PersistentDataType.TAG_CONTAINER, "components"));
}
