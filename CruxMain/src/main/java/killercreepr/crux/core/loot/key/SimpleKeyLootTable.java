package killercreepr.crux.core.loot.key;

import killercreepr.crux.api.loot.LootPool;
import killercreepr.crux.api.loot.key.KeyLootTable;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.loot.SimpleLootTable;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SimpleKeyLootTable extends SimpleLootTable<Key> implements KeyLootTable {
    public SimpleKeyLootTable(@NotNull Key key, @NotNull NumberProvider rolls, @NotNull List<LootPool<Key>> lootPools) {
        super(key, rolls, lootPools);
    }
}
