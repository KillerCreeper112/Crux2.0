package killercreepr.cruxadvancements.advancement.objective;

import killercreepr.cruxadvancements.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.advancement.objective.progress.NumberObjectiveProgress;
import killercreepr.cruxadvancements.advancement.objective.progress.ObjectiveProgress;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class NumberObjective extends SimpleAdvancementObjective {
    protected final int maxProgress;
    public NumberObjective(@NotNull String criterion, int maxProgress) {
        super(criterion);
        this.maxProgress = maxProgress;
    }

    public int getMaxProgress() {
        return maxProgress;
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
}
