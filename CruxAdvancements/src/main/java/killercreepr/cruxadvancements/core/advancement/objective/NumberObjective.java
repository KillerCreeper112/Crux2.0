package killercreepr.cruxadvancements.core.advancement.objective;

import killercreepr.crux.api.loot.LootContext;
import killercreepr.cruxadvancements.api.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import killercreepr.cruxadvancements.api.advancement.objective.progress.ObjectiveProgress;
import killercreepr.cruxadvancements.api.advancement.objective.progress.ObjectiveProgression;
import killercreepr.cruxadvancements.api.event.objective.NumberObjectiveProgressChangeEvent;
import killercreepr.cruxadvancements.api.event.objective.PostNumberObjectiveProgressChangeEvent;
import killercreepr.cruxadvancements.core.advancement.criteria.NumberCriteria;
import killercreepr.cruxadvancements.core.advancement.objective.progress.NumberObjectiveProgress;
import org.bukkit.event.Cancellable;
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
        return trigger(who, manager, advancement, ctx, 1);
    }

    public boolean trigger(@NotNull UUID who,
                           @NotNull CruxAdvancementManager manager,
                           @NotNull ObjectiveAdvancement advancement,
                           @NotNull LootContext ctx, int amount){
        if(isDone(who, advancement) || !canTrigger(ctx)) return false;
        addToProgress(who, manager, advancement,amount);
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
            Cancellable criteriaEvent;
            if(advancement.getCriteria() instanceof NumberCriteria){
                criteriaEvent = manager.setCriteriaProgress(who, advancement, advancement.getTotalProgress(who));
            }else{
                criteriaEvent = manager.grantCriteria(who, advancement, getCriterion());
            }
            //revert changes
            if(criteriaEvent != null && criteriaEvent.isCancelled()){
                p.setProgress(event.getOldProgress());
                return;
            }

            if(advancement.isGranted(who)){
                //clean up objective data since it isn't needed anymore.
                advancement.setObjectiveProgress(who, null);
            }
        }

        new PostNumberObjectiveProgressChangeEvent(
            who, manager, advancement, this, p, Math.min(event.getNewProgress(), maxProgress), p.getProgress()
        ).callEvent();
    }

    public boolean isDone(@NotNull UUID who, @NotNull ObjectiveAdvancement advancement){
        if(advancement.isGranted(who)) return true;
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
