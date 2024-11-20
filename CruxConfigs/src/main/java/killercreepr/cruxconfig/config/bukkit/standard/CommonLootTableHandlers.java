package killercreepr.cruxconfig.config.bukkit.standard;

import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.FileSimpleLootTable;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class CommonLootTableHandlers {
    public static final @NotNull FileSimpleLootTable<Key> KEY = new FileSimpleLootTable<>(Key.class, CruxRegistries.KEY_LOOT_TABLE);
    public static final @NotNull FileSimpleLootTable<NumberProvider> NUMBER = new FileSimpleLootTable<>(NumberProvider.class, CruxRegistries.NUMBER_LOOT_TABLE);
}
