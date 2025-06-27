package killercreepr.cruxitems.core.component;

import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.core.component.parser.type.ComponentInputParsers;

public class CruxItemsCompParsers {
    public static final PersistTextParser<VaultBlockLootTable> DISPENSE_BLOCK_LOOT_TABLE = PersistTextParser.mapBuilder(VaultBlockLootTable.class)
        .field("loot_table", TextInputField.field(ComponentInputParsers.ITEM_LOOT_TABLE, VaultBlockLootTable::getLootTable))
        .field("override_vanilla", TextInputField.field(PersistTextParser.BOOLEAN, VaultBlockLootTable::isOverrideVanilla))
        .apply(ctx ->{
            return new VaultBlockLootTable(ctx.get("loot_table"), ctx.getOptional("override_vanilla", true));
        });
}
