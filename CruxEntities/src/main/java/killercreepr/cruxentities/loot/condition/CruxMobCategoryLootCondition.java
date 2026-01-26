package killercreepr.cruxentities.loot.condition;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.loot.conditions.BaseCondition;
import killercreepr.cruxentities.entity.CruxMob;
import killercreepr.cruxentities.registries.CruxEntityRegistries;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class CruxMobCategoryLootCondition extends BaseCondition {
  protected final String category;
  public CruxMobCategoryLootCondition(@NotNull String target, String category) {
    super(target);
    this.category = category;
  }

  @Override
  public boolean test(@NotNull LootContext ctx) {
    Entity entity = ctx.info().get(target, Entity.class);
    if(entity == null) return false;

    if(category != null){
      var cruxMob = CruxEntityRegistries.MOB_CATEGORY.get(Crux.key(category));
      if(cruxMob == null){
        Crux.logWarning("No MobCategory of " + category + " found! " + getClass().getSimpleName());
        return false;
      }
      if(!CruxMob.isInCategory(entity, cruxMob)) return false;
    }


    return true;
  }
}
