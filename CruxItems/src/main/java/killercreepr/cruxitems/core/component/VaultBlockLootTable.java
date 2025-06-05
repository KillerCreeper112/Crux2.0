package killercreepr.cruxitems.core.component;

import killercreepr.crux.api.loot.item.ItemLootTable;

public class VaultBlockLootTable {
    protected final ItemLootTable lootTable;
    protected final boolean overrideVanilla;

    public VaultBlockLootTable(ItemLootTable lootTable, boolean overrideVanilla) {
        this.lootTable = lootTable;
        this.overrideVanilla = overrideVanilla;
    }

    public ItemLootTable getLootTable() {
        return lootTable;
    }

    public boolean isOverrideVanilla() {
        return overrideVanilla;
    }
}
