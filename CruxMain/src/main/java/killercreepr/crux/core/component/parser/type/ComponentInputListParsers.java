package killercreepr.crux.core.component.parser.type;

import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.loot.item.ItemLootTable;
import killercreepr.crux.core.persistence.CruxPersistence;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class ComponentInputListParsers {
    public final PersistTextParser<List<PotionEffect>> POTION_EFFECT = PersistTextParser.list(ComponentInputParsers.POTION_EFFECT);
    public final PersistTextParser<List<PotionEffectType>> POTION_EFFECT_TYPE = PersistTextParser.list(ComponentInputParsers.POTION_EFFECT_TYPE);
    public static PersistTextParser<List<ItemLootTable>> ITEM_LOOT_TABLE = PersistTextParser.list(ComponentInputParsers.ITEM_LOOT_TABLE, CruxPersistence.LIST.ITEM_LOOT_TABLE);
    public final PersistTextParser<List<EntityType>> ENTITY_TYPE = PersistTextParser.list(ComponentInputParsers.ENTITY_TYPE);
    public final PersistTextParser<List<AttributeModifier>> ATTRIBUTE_MODIFIER = PersistTextParser.list(ComponentInputParsers.ATTRIBUTE_MODIFIER);
}
