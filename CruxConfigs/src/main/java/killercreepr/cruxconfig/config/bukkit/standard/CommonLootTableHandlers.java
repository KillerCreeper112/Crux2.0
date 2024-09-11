package killercreepr.cruxconfig.config.bukkit.standard;

import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.FileSimpleLootTable;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class CommonLootTableHandlers {
    public static final @NotNull FileSimpleLootTable<Key> KEY = new FileSimpleLootTable<>(Key.class, CruxRegistries.KEY_LOOT_TABLE);
}
