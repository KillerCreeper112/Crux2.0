package killercreepr.crux.core.loot.number;

import killercreepr.crux.api.loot.LootPool;
import killercreepr.crux.api.loot.number.NumberLootTable;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.loot.SimpleLootTable;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SimpleNumberLootTable extends SimpleLootTable<NumberProvider> implements NumberLootTable {
    public SimpleNumberLootTable(@NotNull Key key, @NotNull NumberProvider rolls, @NotNull List<LootPool<NumberProvider>> lootPools) {
        super(key, rolls, lootPools);
    }
}
