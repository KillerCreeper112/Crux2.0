package killercreepr.cruxconfig.config.bukkit.standard;

import killercreepr.crux.api.entity.CruxEntitySnapshot;
import killercreepr.crux.api.loot.LootPool;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.loot.SimpleLootTable;
import killercreepr.crux.core.loot.entity.SimpleCruxEntitySnapshotLootTable;
import killercreepr.crux.core.loot.key.SimpleKeyLootTable;
import killercreepr.crux.core.loot.number.SimpleNumberLootTable;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.FileSimpleLootTable;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CommonLootTableHandlers {
    public static final @NotNull FileSimpleLootTable<Key> KEY = new FileSimpleLootTable<>(Key.class, CruxRegistries.KEY_LOOT_TABLE){
        @Override
        public @Nullable SimpleLootTable<Key> createLootTable(@NotNull Key key, @NotNull NumberProvider rolls, @NotNull List<LootPool<Key>> lootPools) {
            return new SimpleKeyLootTable(key, rolls, lootPools);
        }
    };
    public static final @NotNull FileSimpleLootTable<NumberProvider> NUMBER = new FileSimpleLootTable<>(NumberProvider.class, CruxRegistries.NUMBER_LOOT_TABLE){
        @Override
        public @Nullable SimpleLootTable<NumberProvider> createLootTable(@NotNull Key key, @NotNull NumberProvider rolls, @NotNull List<LootPool<NumberProvider>> lootPools) {
            return new SimpleNumberLootTable(key, rolls, lootPools);
        }
    };
    public static final @NotNull FileSimpleLootTable<CruxEntitySnapshot> CRUX_ENTITY_SNAPSHOT = new FileSimpleLootTable<>(CruxEntitySnapshot.class, CruxRegistries.CRUX_ENTITY_SNAPSHOT_LOOT_TABLE){
        @Override
        public @Nullable SimpleLootTable<CruxEntitySnapshot> createLootTable(@NotNull Key key, @NotNull NumberProvider rolls, @NotNull List<LootPool<CruxEntitySnapshot>> lootPools) {
            return new SimpleCruxEntitySnapshotLootTable(key, rolls, lootPools);
        }
    };
}
