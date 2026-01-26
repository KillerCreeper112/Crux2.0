package killercreepr.cruxentities.config.hook;

import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.bukkit.handler.BukkitCfgHandlers;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.SimpleFileLootCondition;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxentities.loot.condition.CruxMobCategoryLootCondition;
import killercreepr.cruxentities.loot.condition.CruxMobLootCondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxEntitiesCfgHook {
  public void register(){
    var lootConditions = BukkitCfgHandlers.LOOT_CONDITION;
    lootConditions.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("is_crux_mob")) {
      @Override
      public @Nullable LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
        var r = ctx.getRegistry();
        return new CruxMobLootCondition(
          target,
          r.deserializeFromFile(String.class, e.get("crux_mob"))
        );
      }
    });
    lootConditions.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("is_in_mob_category")) {
      @Override
      public @Nullable LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
        var r = ctx.getRegistry();
        return new CruxMobCategoryLootCondition(
          target,
          r.deserializeFromFile(String.class, e.get("mob_category"))
        );
      }
    });
  }
}
