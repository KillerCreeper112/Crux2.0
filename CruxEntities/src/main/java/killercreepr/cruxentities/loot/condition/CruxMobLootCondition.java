package killercreepr.cruxentities.loot.condition;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.loot.conditions.BaseCondition;
import killercreepr.cruxentities.entity.CruxMob;
import killercreepr.cruxentities.registries.CruxEntityRegistries;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class CruxMobLootCondition extends BaseCondition {
  protected final String id;
  public CruxMobLootCondition(@NotNull String target, String id) {
    super(target);
    this.id = id;
  }

  @Override
  public boolean test(@NotNull LootContext ctx) {
    Entity entity = ctx.info().get(target, Entity.class);
    if(entity == null) return false;

    if(id != null){
      var cruxMob = CruxEntityRegistries.ENTITIES.get(Crux.key(id));
      if(cruxMob == null){
        Crux.logWarning("No CruxMob of " + id + " found! " + getClass().getSimpleName());
        return false;
      }
      if(!CruxMob.is(entity, cruxMob)) return false;
    }else if(!CruxMob.is(entity)) return false;


    return true;
  }
}
