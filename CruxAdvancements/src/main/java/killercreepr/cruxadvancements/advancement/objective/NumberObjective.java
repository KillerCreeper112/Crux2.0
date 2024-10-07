package killercreepr.cruxadvancements.advancement.objective;

import killercreepr.crux.loot.LootContext;
import killercreepr.cruxadvancements.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.advancement.criteria.NumberCriteria;
import killercreepr.cruxadvancements.advancement.objective.progress.NumberObjectiveProgress;
import killercreepr.cruxadvancements.advancement.objective.progress.ObjectiveProgress;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class NumberObjective extends SimpleAdvancementObjective {
    protected final int maxProgress;
    public NumberObjective(@NotNull ObjectiveCommonData data, int maxProgress) {
        super(data);
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
            advancement.getObjectiveProgress(who).getProgress(getCriterion()).toType(NumberObjectiveProgress.class),
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
        if(shouldUpdateAdvancement(advancement,p)){
            if(advancement.getCriteria() instanceof NumberCriteria){
                manager.setCriteriaProgress(who, advancement, p.getProgress());
            }else manager.grantCriteria(who, advancement, getCriterion());
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
    public boolean shouldUpdateAdvancement(@NotNull ObjectiveAdvancement advancement, @NotNull ObjectiveProgress progress) {
        if(advancement.getCriteria() instanceof NumberCriteria){
            if(advancement.getUpdateAdvancementPeriod() == 0) return true;
            return progress.toType(NumberObjectiveProgress.class).getProgress() % advancement.getUpdateAdvancementPeriod() == 0;
        }
        return isDone(progress);
    }

    @Override
    public @NotNull ObjectiveProgress createNewProgress() {
        return new NumberObjectiveProgress();
    }
}
