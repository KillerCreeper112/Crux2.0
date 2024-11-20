package killercreepr.cruxconfig.config.bukkit.loader;

import killercreepr.crux.core.Crux;
import killercreepr.crux.api.loot.LootTable;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.FileSimpleLootTable;
import killercreepr.cruxconfig.config.bukkit.standard.CommonLootTableHandlers;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class NumberLootTableLoader extends CfgLoader {
    protected final @NotNull FileSimpleLootTable<NumberProvider> fileSimpleLootTable;
    public NumberLootTableLoader(@NotNull FileSimpleLootTable<NumberProvider> fileSimpleLootTable) {
        this.fileSimpleLootTable = fileSimpleLootTable;
    }

    public NumberLootTableLoader() {
        this(CommonLootTableHandlers.NUMBER);
    }

    @Override
    public void loadConfiguration(@NotNull DataFile cfg, @Nullable String path){
        LootTable<NumberProvider> table;
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
        Crux.log(Level.INFO, "Registered number loot table: " + table.key());
        CruxRegistries.NUMBER_LOOT_TABLE.register(table);
    }
}
