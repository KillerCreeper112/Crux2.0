package killercreepr.cruxworlds.core.config.loader;

import killercreepr.crux.api.loot.LootPool;
import killercreepr.crux.api.loot.LootTable;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.loot.SimpleLootTable;
import killercreepr.crux.core.loot.number.SimpleNumberLootTable;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.FileSimpleLootTable;
import killercreepr.cruxconfig.config.bukkit.loader.CfgLoader;
import killercreepr.cruxconfig.config.bukkit.standard.CommonLootTableHandlers;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawnGroup;
import killercreepr.cruxworlds.core.loot.SimpleNaturalEntitySpawnGroupLootTable;
import killercreepr.cruxworlds.core.registries.WorldsRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;

public class EntitySpawnGroupLootTableLoader extends CfgLoader {

    public static final @NotNull FileSimpleLootTable<NaturalEntitySpawnGroup> STANDARD = new FileSimpleLootTable<>(NaturalEntitySpawnGroup.class, WorldsRegistries.NATURAL_ENTITY_SPAWN_GROUP_LOOT_TABLE){
        @Override
        public @Nullable SimpleLootTable<NaturalEntitySpawnGroup> createLootTable(@NotNull Key key, @NotNull NumberProvider rolls, @NotNull List<LootPool<NaturalEntitySpawnGroup>> lootPools) {
            return new SimpleNaturalEntitySpawnGroupLootTable(key, rolls, lootPools);
        }
    };

    protected final @NotNull FileSimpleLootTable<NaturalEntitySpawnGroup> fileSimpleLootTable;
    public EntitySpawnGroupLootTableLoader(@NotNull FileSimpleLootTable<NaturalEntitySpawnGroup> fileSimpleLootTable) {
        this.fileSimpleLootTable = fileSimpleLootTable;
    }

    public EntitySpawnGroupLootTableLoader() {
        this(STANDARD);
    }

    @Override
    public void loadConfiguration(@NotNull DataFile cfg, @Nullable String path){
        LootTable<NaturalEntitySpawnGroup> table;
        if(path == null){
            if(!(cfg.getRoot() instanceof FileObject root)) return;

            table = fileSimpleLootTable.deserializeFromFile(
                new FileContext<>(cfg.fileRegistry()), root
            );
        } else{
            if(!(cfg.getRoot() instanceof FileObject root)) return;

            table = fileSimpleLootTable.deserializeFromFile(
                new FileContext<>(cfg.fileRegistry()), root, Crux.key(path)
            );
        }
        if(table == null) return;
        Crux.log(Level.INFO, "Registered natural entity spawn group loot table: " + table.key());
        WorldsRegistries.NATURAL_ENTITY_SPAWN_GROUP_LOOT_TABLE.register(table);
    }
}
