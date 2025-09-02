package killercreepr.crux.core.loot.entity;

import killercreepr.crux.api.entity.CruxEntitySnapshot;
import killercreepr.crux.api.loot.LootPool;
import killercreepr.crux.api.loot.entity.CruxEntitySnapshotLootTable;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.loot.SimpleLootTable;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SimpleCruxEntitySnapshotLootTable extends SimpleLootTable<CruxEntitySnapshot> implements CruxEntitySnapshotLootTable {
    public SimpleCruxEntitySnapshotLootTable(@NotNull Key key, @NotNull NumberProvider rolls, @NotNull List<LootPool<CruxEntitySnapshot>> lootPools) {
        super(key, rolls, lootPools);
    }
}
