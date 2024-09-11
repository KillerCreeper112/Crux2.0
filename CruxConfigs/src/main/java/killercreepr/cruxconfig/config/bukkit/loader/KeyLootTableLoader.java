package killercreepr.cruxconfig.config.bukkit.loader;

import killercreepr.crux.Crux;
import killercreepr.crux.loot.LootTable;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.FileSimpleLootTable;
import killercreepr.cruxconfig.config.bukkit.standard.CommonLootTableHandlers;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class KeyLootTableLoader extends CfgLoader {
    protected final @NotNull FileSimpleLootTable<Key> fileSimpleLootTable;
    public KeyLootTableLoader(@NotNull FileSimpleLootTable<Key> fileSimpleLootTable) {
        this.fileSimpleLootTable = fileSimpleLootTable;
    }

    public KeyLootTableLoader() {
        this(CommonLootTableHandlers.KEY);
    }

    @Override
    public void loadConfiguration(@NotNull DataFile cfg, @Nullable String path){
        LootTable<Key> table;
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
        Crux.log(Level.INFO, "Registered key loot table: " + table.key());
        CruxRegistries.KEY_LOOT_TABLE.register(table);
    }
}
