package killercreepr.crux.core.component.parser.type;

import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.block.tag.BlockTag;
import killercreepr.crux.api.component.parser.StringListEncodeComponent;
import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.api.item.component.ToolComponent;
import killercreepr.crux.api.item.predicate.ItemPredicate;
import killercreepr.crux.api.item.tag.ItemTag;
import killercreepr.crux.api.loot.item.ItemLootTable;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.component.parser.hybrid.text.ListPersistTextParser;
import killercreepr.crux.core.item.predicate.ItemAllPredicate;
import killercreepr.crux.core.item.predicate.ItemAnyPredicate;
import killercreepr.crux.core.persistence.CruxPersistence;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.crux.paper.ItemHolder;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

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

    public static PersistTextParser<ItemPredicate> SIMPLE_ITEM_PREDICATE = PersistTextParser.elementBuilder(ItemPredicate.class)
        .field(TextInputField.field(PersistTextParser.STRING, e ->{
            if(!(e instanceof StringListEncodeComponent all)) throw new IllegalArgumentException(
                "ItemPredicate must implement StringListEncode! " + e
            );
            return all.encodeToParser().getFirst();
        }))
        .apply(ctx ->{
            String id = ctx.get();
            boolean invert = id.startsWith("!");
            if(invert) id = id.substring(1);
            ItemPredicate p;
            if(id.startsWith("#")){
                ItemTag tag = CruxRegistries.ITEM_TAG.get(Crux.key(id.substring(1)));
                if(tag == null) return null;
                p = ItemPredicate.fromTag(tag);
            }else{
                p = ItemPredicate.fromType(Crux.key(id));
            }
            return invert ? ItemPredicate.fromInverted(p) : p;
        });

    public static PersistTextParser<ItemPredicate> ITEM_PREDICATE = PersistTextParser.mapBuilder(ItemPredicate.class)
        .field("type", TextInputField.field(PersistTextParser.STRING, e ->{
            if(e instanceof ItemAllPredicate) return "all_of";
            if(e instanceof ItemAnyPredicate) return "any_of";
            return null;
        }))
        .field("terms", TextInputField.field(PersistTextParser.LIST.STRING, e ->{
            if(!(e instanceof StringListEncodeComponent c)) return null;
            return c.encodeToParser();
        }))
        .apply(ctx ->{
            if(!(ctx.getOptional("terms") instanceof List<?> list)){
                return SIMPLE_ITEM_PREDICATE.decodeObject(ctx.get());
            }

            Collection<ItemPredicate> parsed = new HashSet<>();
            for(Object s : list){
                ItemPredicate predicate = SIMPLE_ITEM_PREDICATE.decodeObject(s);
                parsed.add(predicate);
            }
            String type = ctx.getOptional("type", "all_of");
            switch (type.toLowerCase()){
                case "all_of" ->{
                    return ItemPredicate.fromAllOf(parsed);
                }
                case "any_of" ->{
                    return ItemPredicate.fromAnyOf(parsed);
                }
            }
            return ItemPredicate.fromAllOf(parsed);
        });

    public static PersistTextParser<BlockPredicate> SIMPLE_BLOCK_PREDICATE = PersistTextParser.elementBuilder(BlockPredicate.class)
        .field(TextInputField.field(PersistTextParser.STRING, e ->{
            if(!(e instanceof StringListEncodeComponent all)) throw new IllegalArgumentException(
                "BlockPredicate must be a StringListEncodeComponent! " + e
            );
            return all.encodeToParser().getFirst();
        }))
        .apply(ctx ->{
            String id = ctx.get();
            boolean invert = id.startsWith("!");
            if(invert) id = id.substring(1);
            BlockPredicate p;
            if(id.startsWith("#")){
                BlockTag tag = CruxRegistries.BLOCK_TAG.get(Crux.key(id.substring(1)));
                if(tag == null) return null;
                p = BlockPredicate.fromTag(tag);
            }else p = BlockPredicate.fromType(Crux.key(id));
            return invert ? BlockPredicate.fromInverted(p) : p;
        });

    public static PersistTextParser<BlockPredicate> BLOCK_PREDICATE = PersistTextParser.elementBuilder(BlockPredicate.class)
        .field(TextInputField.field(PersistTextParser.LIST.STRING, e ->{
            if(!(e instanceof StringListEncodeComponent all)) throw new IllegalArgumentException(
                "BlockPredicate must be a BlockPredicateComponent! " + e
            );
            return all.encodeToParser();
        }))
        .apply(ctx ->{
            if(!(ctx.get() instanceof List<?>)){
                return SIMPLE_BLOCK_PREDICATE.decodeObject(ctx.get());
            }
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
