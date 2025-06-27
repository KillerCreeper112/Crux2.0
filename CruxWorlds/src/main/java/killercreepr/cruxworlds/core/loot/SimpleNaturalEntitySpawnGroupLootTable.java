package killercreepr.cruxworlds.core.loot;

import killercreepr.crux.api.loot.LootPool;
import killercreepr.crux.api.loot.number.NumberLootTable;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.loot.SimpleLootTable;
import killercreepr.cruxworlds.api.loot.NaturalEntitySpawnGroupLootTable;
import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawnGroup;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SimpleNaturalEntitySpawnGroupLootTable extends SimpleLootTable<NaturalEntitySpawnGroup> implements NaturalEntitySpawnGroupLootTable {
    public SimpleNaturalEntitySpawnGroupLootTable(@NotNull Key key, @NotNull NumberProvider rolls, @NotNull List<LootPool<NaturalEntitySpawnGroup>> lootPools) {
        super(key, rolls, lootPools);
    }
}
