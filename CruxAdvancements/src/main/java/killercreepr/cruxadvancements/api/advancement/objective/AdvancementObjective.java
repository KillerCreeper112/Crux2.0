package killercreepr.cruxadvancements.api.advancement.objective;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.cruxadvancements.api.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.api.advancement.objective.progress.ObjectiveProgress;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface AdvancementObjective {
    default boolean canTrigger(@NotNull LootContext ctx){
        LootCondition conditions = getConditions();
        if(conditions==null) return true;
        return conditions.test(ctx);
    }

    @NotNull
    String getCriterion();
    boolean isDone(@NotNull ObjectiveProgress progress);
    boolean shouldUpdateAdvancement(@NotNull ObjectiveAdvancement advancement, @NotNull ObjectiveProgress progress);
    @NotNull ObjectiveProgress createNewProgress();
    @Nullable
    LootCondition getConditions();
}
