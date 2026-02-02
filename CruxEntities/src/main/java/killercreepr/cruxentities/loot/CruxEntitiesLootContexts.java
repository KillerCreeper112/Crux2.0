package killercreepr.cruxentities.loot;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.cruxentities.api.event.EntityPathTargetEvent;
import killercreepr.cruxentities.api.event.EntityPathTargetFinishEvent;
import killercreepr.cruxentities.api.event.EntityPathTargetStartEvent;
import org.jetbrains.annotations.NotNull;

public class CruxEntitiesLootContexts {
  public static LootContext.Builder builder(@NotNull EntityPathTargetFinishEvent event){
    return builder((EntityPathTargetEvent) event);
  }
  public static LootContext.Builder builder(@NotNull EntityPathTargetStartEvent event){
    return builder((EntityPathTargetEvent) event);
  }
  private static LootContext.Builder builder(@NotNull EntityPathTargetEvent event){
    var e = event.getEntity();
    var goal = event.getGoal();
    return builder()
      .info(
        DataExchange.builder()
          .build()
      )
      .location(e.getLocation())
      .looter(e)
      .looted(goal)
      ;
  }


  private static LootContext.Builder builder(){
    return LootContext.builder();
  }
}
