package killercreepr.cruxitems.core.component;

import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.component.parser.InputDecodeContext;
import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.api.item.predicate.ItemPredicate;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.component.parser.type.ComponentInputParsers;
import killercreepr.crux.core.registries.CruxRegistries;

import java.util.function.UnaryOperator;

public class CruxItemsComponents {
    public static void register(){}

    public static final DataComponentType<ItemPredicate> VAULT_BLOCK_KEY = register("vault_block_key", builder -> builder
        .persistTextParser(PersistTextParser.elementBuilder(ItemPredicate.class)
            .field(TextInputField.field(
                ComponentInputParsers.ITEM_PREDICATE, e -> e
            ))
            .apply(InputDecodeContext::get)
            .createInput(Crux.key("vault_block_key"))));

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(Crux.key(id), builderOperator.apply(DataComponentType.builder()).build());
    }
}
