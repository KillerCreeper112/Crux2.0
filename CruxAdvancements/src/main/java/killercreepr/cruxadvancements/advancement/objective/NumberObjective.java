package killercreepr.cruxadvancements.advancement.objective;

import killercreepr.crux.loot.api.LootContext;
import killercreepr.crux.loot.api.conditions.LootCondition;
import killercreepr.cruxadvancements.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.advancement.objective.progress.NumberObjectiveProgress;
import killercreepr.cruxadvancements.advancement.objective.progress.ObjectiveProgress;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class NumberObjective extends SimpleAdvancementObjective {
    protected final int maxProgress;
    public NumberObjective(@NotNull String criterion, @Nullable LootCondition conditions, int maxProgress) {
        super(criterion, conditions);
        this.maxProgress = maxProgress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    @SuppressWarnings("rawtypes")
    public boolean trigger(@NotNull UUID who,
                           @NotNull CruxAdvancementManager manager,
                           @NotNull ObjectiveAdvancement advancement,
                           @NotNull LootContext ctx){
        if(!canTrigger(ctx)) return false;
        addToProgress(who, manager, advancement,1);
        return true;
    }

    @SuppressWarnings("rawtypes")
    public void addToProgress(@NotNull UUID who,
                              @NotNull CruxAdvancementManager manager,
                              @NotNull ObjectiveAdvancement advancement,
                              int amount){
        addToProgress(who, manager, advancement,
            advancement.getObjectiveProgress(who).getProgress(criterion).toType(NumberObjectiveProgress.class),
            amount
        );
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void addToProgress(@NotNull UUID who,
                              @NotNull CruxAdvancementManager manager,
                              @NotNull ObjectiveAdvancement advancement,
                              @NotNull ObjectiveProgress progress,
                              int amount){
        NumberObjectiveProgress p = progress.toType(NumberObjectiveProgress.class);
        p.setProgress(p.getProgress()+amount);
        if(shouldUpdateAdvancement(p)){
            manager.grantCriteria(who, advancement, criterion);
            if(advancement.isGranted(who)){
                //clean up objective data since it isn't needed anymore.
                advancement.setObjectiveProgress(who, null);
            }
        }
    }

    @Override
    public boolean isDone(@NotNull ObjectiveProgress progress) {
        return progress.toType(NumberObjectiveProgress.class).getProgress() >= maxProgress;
    }

    @Override
    public boolean shouldUpdateAdvancement(@NotNull ObjectiveProgress progress) {
        return isDone(progress);
    }

    @Override
    public @NotNull ObjectiveProgress createNewProgress() {
        return new NumberObjectiveProgress();
    }
}
