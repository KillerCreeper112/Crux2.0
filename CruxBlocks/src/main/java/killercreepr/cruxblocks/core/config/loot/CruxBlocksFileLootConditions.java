package killercreepr.cruxblocks.core.config.loot;

import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.core.Crux;
import killercreepr.cruxblocks.core.loot.conditions.block.CruxBlockCondition;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.FileLootCondition;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.SimpleFileLootCondition;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileObject;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class CruxBlocksFileLootConditions {
    public static void register(@NotNull FileLootCondition file) {
        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("crux_block")) {
            @Override
            public @NotNull LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry registry = ctx.getRegistry();
                Key type = registry.deserializeFromFile(Key.class, e.get("block"));
                return new CruxBlockCondition(target, type);
            }
        });
    }
}
