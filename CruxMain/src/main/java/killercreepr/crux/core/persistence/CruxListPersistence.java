package killercreepr.crux.core.persistence;

import killercreepr.crux.api.item.component.ToolComponent;
import killercreepr.crux.api.loot.item.ItemLootTable;
import killercreepr.crux.core.persistence.type.ListTagType;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.potion.PotionEffect;

public class CruxListPersistence {
    public final ListTagType<byte[], java.util.UUID> UUID = new ListTagType<>(CruxPersistence.UUID);
    public final ListTagType<PersistentDataContainer, PotionEffect> POTION_EFFECT = new ListTagType<>(CruxPersistence.POTION_EFFECT);
    public final ListTagType<PersistentDataContainer, Location> LOCATION = new ListTagType<>(CruxPersistence.LOCATION);
    public final ListTagType<PersistentDataContainer, ToolComponent> TOOL_COMPONENT = new ListTagType<>(CruxPersistence.TOOL_COMPONENT);
    public final ListTagType<String, Key> KEY = new ListTagType<>(CruxPersistence.CRUX_KEY);
    public final ListTagType<String, ItemStack> ITEM_STACK = new ListTagType<>(CruxPersistence.ITEM_STACK);
    public final ListTagType<PersistentDataContainer, ToolComponent.Rule> TOOL_COMPONENT_RULE = new ListTagType<>(CruxPersistence.TOOL_COMPONENT_RULE);
    public final ListTagType<String, ItemLootTable> ITEM_LOOT_TABLE = new ListTagType<>(CruxPersistence.ITEM_LOOT_TABLE);
}
