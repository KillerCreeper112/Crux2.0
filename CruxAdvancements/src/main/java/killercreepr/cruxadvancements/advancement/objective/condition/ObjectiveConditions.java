package killercreepr.cruxadvancements.advancement.objective.condition;

import killercreepr.crux.loot.api.LootContext;
import org.jetbrains.annotations.NotNull;

public interface ObjectiveConditions {
    boolean test(@NotNull LootContext ctx);
}
