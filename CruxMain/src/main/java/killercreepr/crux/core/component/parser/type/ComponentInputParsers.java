package killercreepr.crux.core.component.parser.type;

import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.block.predicate.BlockPredicateComponent;
import killercreepr.crux.api.component.parser.hybrid.PersistTextInputParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.api.item.component.ToolComponent;
import killercreepr.crux.core.block.predicate.BlockAllPredicate;
import killercreepr.crux.core.component.parser.hybrid.text.ListPersistTextInputParser;

public class ComponentInputParsers {
    public static PersistTextInputParser<BlockPredicate> BLOCK_PREDICATE = PersistTextInputParser.elementBuilder(BlockPredicate.class)
        .field(TextInputField.field(PersistTextInputParser.LIST.STRING, e ->{
            if(!(e instanceof BlockPredicateComponent all)) throw new IllegalArgumentException(
                "BlockPredicate must be a BlockPredicateComponent! " + e
            );
            return all.encodeToParser();
        }))
        .apply(ctx -> new BlockAllPredicate(ctx.get()))
        ;

    public static PersistTextInputParser<ToolComponent.Rule> TOOL_RULE = PersistTextInputParser.mapBuilder(ToolComponent.Rule.class)
        .field("blocks", TextInputField.field(BLOCK_PREDICATE, ToolComponent.Rule::getBlocks))
        .field("speed", TextInputField.field(PersistTextInputParser.FLOAT, ToolComponent.Rule::getSpeed))
        .field("correct_for_drops", TextInputField.field(PersistTextInputParser.BOOLEAN, ToolComponent.Rule::getCorrectForDrops))
        .apply(ctx -> new ToolComponent.Simple.Rule(ctx.getOptional("blocks"), ctx.getOptional("speed"), ctx.getOptional("correct_for_drops")));

    public static PersistTextInputParser<ToolComponent> TOOL = PersistTextInputParser.mapBuilder(ToolComponent.class)
        .field("default_mining_speed", TextInputField.field(PersistTextInputParser.FLOAT, ToolComponent::getDefaultMiningSpeed))
        .field("rules", TextInputField.field(new ListPersistTextInputParser<>(TOOL_RULE, null), ToolComponent::getRules))
        .apply(ctx -> new ToolComponent.Simple(ctx.getOptional("default_mining_speed", 1f), ctx.getOptional("rules")));
}
