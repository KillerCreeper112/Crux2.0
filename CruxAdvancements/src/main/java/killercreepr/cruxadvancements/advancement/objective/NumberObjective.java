package killercreepr.cruxadvancements.advancement.objective;

import killercreepr.crux.loot.LootContext;
import killercreepr.cruxadvancements.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.advancement.criteria.NumberCriteria;
import killercreepr.cruxadvancements.advancement.objective.progress.NumberObjectiveProgress;
import killercreepr.cruxadvancements.advancement.objective.progress.ObjectiveProgress;
import killercreepr.cruxadvancements.advancement.objective.progress.ObjectiveProgression;
import killercreepr.cruxadvancements.event.objective.NumberObjectiveProgressChangeEvent;
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
        if(isDone(who, advancement) || !canTrigger(ctx)) return false;
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

        NumberObjectiveProgressChangeEvent event = new NumberObjectiveProgressChangeEvent(
            who, manager, advancement, this, p, p.getProgress()+amount, p.getProgress()
        );
        if(!event.callEvent()) return;

        p.setProgress(Math.min(event.getNewProgress(), maxProgress));

        if(shouldUpdateAdvancement(advancement,p)){
            if(advancement.getCriteria() instanceof NumberCriteria){
                manager.setCriteriaProgress(who, advancement, getTotalProgress(who, advancement));
            }else manager.grantCriteria(who, advancement, getCriterion());
            if(advancement.isGranted(who)){
                //clean up objective data since it isn't needed anymore.
                advancement.setObjectiveProgress(who, null);
            }
        }
    }

    public int getTotalProgress(@NotNull UUID who, @NotNull ObjectiveAdvancement advancement){
        int total = 0;
        ObjectiveProgression progression = advancement.getObjectiveProgressIfPresent(who);
        if(progression == null) return total;

        for(AdvancementObjective obj : advancement.getObjectives().values()){
            if(!(obj instanceof NumberObjective numberObjective)) continue;
            ObjectiveProgress progress = progression.getProgressIfPresent(obj.getCriterion());
            if(!(progress instanceof NumberObjectiveProgress pro)) continue;
            total += pro.getProgress();
        }
        return total;
    }

    public boolean isDone(@NotNull UUID who, @NotNull ObjectiveAdvancement advancement){
        ObjectiveProgression progression = advancement.getObjectiveProgressIfPresent(who);
        if(progression == null) return false;
        ObjectiveProgress progress = progression.getProgressIfPresent(getCriterion());
        if(progress == null) return false;
        return isDone(progress);
    }

    @Override
    public boolean isDone(@NotNull ObjectiveProgress progress) {
        return progress.toType(NumberObjectiveProgress.class).getProgress() >= maxProgress;
    }

    @Override
    public boolean shouldUpdateAdvancement(@NotNull ObjectiveAdvancement advancement, @NotNull ObjectiveProgress progress) {
        if(isDone(progress)) return true;
        if(advancement.getCriteria() instanceof NumberCriteria){
            if(advancement.getUpdateAdvancementPeriod() == 0) return true;
            return progress.toType(NumberObjectiveProgress.class).getProgress() % advancement.getUpdateAdvancementPeriod() == 0;
        }
        return false;
    }

    @Override
    public @NotNull ObjectiveProgress createNewProgress() {
        return new NumberObjectiveProgress();
    }
}
