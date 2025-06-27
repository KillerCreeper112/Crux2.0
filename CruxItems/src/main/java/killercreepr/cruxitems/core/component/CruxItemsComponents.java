package killercreepr.cruxitems.core.component;

import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.component.parser.InputDecodeContext;
import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.component.parser.type.ComponentInputParsers;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.crux.paper.ItemHolder;
import net.kyori.adventure.key.Key;

import java.util.function.UnaryOperator;

public class CruxItemsComponents {
    public static void register(){}

    public static final DataComponentType<ItemHolder> VAULT_BLOCK_KEY = register("vault_block_key", builder -> builder
        .persistTextParser(PersistTextParser.elementBuilder(ItemHolder.class)
            .field(TextInputField.field(
                ComponentInputParsers.ITEM_HOLDER, e -> e
            ))
            .apply(InputDecodeContext::get)
            .createInput(Crux.key("vault_block_key"))));

    public static final DataComponentType<VaultBlockLootTable> DISPENSE_BLOCK_LOOT_TABLE = register("dispense_block_loot_table", builder -> builder
        .persistTextParser(CruxItemsCompParsers.DISPENSE_BLOCK_LOOT_TABLE
            .createInput(Crux.key("dispense_block_loot_table"))));

    public static final DataComponentType<VaultBlockLootTable> OMINOUS_DISPENSE_BLOCK_LOOT_TABLE = register("ominous_dispense_block_loot_table", builder -> builder
        .persistTextParser(CruxItemsCompParsers.DISPENSE_BLOCK_LOOT_TABLE
            .createInput(Crux.key("ominous_dispense_block_loot_table"))));


    public static final DataComponentType<Key> PLUGIN_ITEM_REFERENCE = register("plugin_item_reference", builder -> builder
        .persistTextParser(PersistTextParser.KEY.createInput(Crux.key("plugin_item_reference"))));

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return CruxRegistries.DATA_COMPONENT_TYPE.register(Crux.key(id), builderOperator.apply(DataComponentType.builder()).build());
    }
}
