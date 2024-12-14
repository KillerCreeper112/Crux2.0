package killercreepr.crux.core.component.parser.type;

import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.block.predicate.BlockPredicateComponent;
import killercreepr.crux.api.block.tag.BlockTag;
import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.api.item.component.ToolComponent;
import killercreepr.crux.api.loot.item.ItemLootTable;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.block.predicate.BlockAllPredicate;
import killercreepr.crux.core.component.parser.hybrid.text.ListPersistTextParser;
import killercreepr.crux.core.persistence.CruxPersist;
import killercreepr.crux.core.persistence.CruxPersistence;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.crux.paper.ItemHolder;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;

import java.util.*;

public class ComponentInputParsers {
    public static PersistTextParser<ItemHolder> ITEM_HOLDER = PersistTextParser.elementBuilder(ItemHolder.class)
        .field(TextInputField.field(PersistTextParser.KEY, ItemHolder::key))
        .apply(ctx ->{
            Key key = ctx.get();
            return Objects.requireNonNull(
                Crux.handlers().item().getItem(key),
                "ItemHolder of " + key + " not found!"
            );
        });

    public static PersistTextParser<ItemLootTable> ITEM_LOOT_TABLE = PersistTextParser.elementBuilder(ItemLootTable.class)
        .field(TextInputField.field(PersistTextParser.KEY, Keyed::key))
        .apply(ctx ->{
            Key key = ctx.get();
            return (ItemLootTable) Objects.requireNonNull(
                CruxRegistries.ITEM_LOOT_TABLE.get(key),
                "ItemLootTable of " + key + " not found!"
            );
        });

    public static PersistTextParser<List<ItemLootTable>> ITEM_LOOT_TABLE_LIST = PersistTextParser.list(ITEM_LOOT_TABLE, CruxPersistence.LIST.ITEM_LOOT_TABLE);

    public static PersistTextParser<BlockPredicate> SIMPLE_BLOCK_PREDICATE = PersistTextParser.elementBuilder(BlockPredicate.class)
        .field(TextInputField.field(PersistTextParser.STRING, e ->{
            if(!(e instanceof BlockPredicateComponent all)) throw new IllegalArgumentException(
                "BlockPredicate must be a BlockPredicateComponent! " + e
            );
            return all.encodeToParser().getFirst();
        }))
        .apply(ctx ->{
            String id = ctx.get();
            if(id.startsWith("#")){
                BlockTag tag = CruxRegistries.BLOCK_TAG.get(Crux.key(id.substring(1)));
                if(tag == null) return null;
                return BlockPredicate.fromTag(tag);
            }
            return BlockPredicate.fromType(Crux.key(id));
        });

    public static PersistTextParser<BlockPredicate> BLOCK_PREDICATE = PersistTextParser.elementBuilder(BlockPredicate.class)
        .field(TextInputField.field(PersistTextParser.LIST.STRING, e ->{
            if(!(e instanceof BlockAllPredicate all)) throw new IllegalArgumentException(
                "BlockPredicate must be a BlockPredicateComponent! " + e
            );
            return all.encodeToParser();
        }))
        .apply(ctx ->{
            Collection<String> list = ctx.get();
            Collection<BlockPredicate> parsed = new HashSet<>();
            for(String s : list){
                BlockPredicate predicate = SIMPLE_BLOCK_PREDICATE.decodeObject(s);
                parsed.add(predicate);
            }
            return BlockPredicate.fromAllOf(parsed);
        });

    public static PersistTextParser<ToolComponent.Rule> TOOL_RULE = PersistTextParser.mapBuilder(ToolComponent.Rule.class)
        .field("blocks", TextInputField.field(BLOCK_PREDICATE, ToolComponent.Rule::getBlocks))
        .field("speed", TextInputField.field(PersistTextParser.FLOAT, ToolComponent.Rule::getSpeed))
        .field("correct_for_drops", TextInputField.field(PersistTextParser.BOOLEAN, ToolComponent.Rule::getCorrectForDrops))
        .apply(ctx -> new ToolComponent.Simple.Rule(ctx.getOptional("blocks"), ctx.getOptional("speed"), ctx.getOptional("correct_for_drops")));

    public static PersistTextParser<ToolComponent> TOOL = PersistTextParser.mapBuilder(ToolComponent.class)
        .field("default_mining_speed", TextInputField.field(PersistTextParser.FLOAT, ToolComponent::getDefaultMiningSpeed))
        .field("rules", TextInputField.field(new ListPersistTextParser<>(TOOL_RULE, null), ToolComponent::getRules))
        .apply(ctx -> new ToolComponent.Simple(ctx.getOptional("default_mining_speed", 1f), ctx.getOptional("rules")));
}
