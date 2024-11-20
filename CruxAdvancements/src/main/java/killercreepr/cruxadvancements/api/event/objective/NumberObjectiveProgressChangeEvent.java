package killercreepr.cruxadvancements.api.event.objective;

import killercreepr.cruxadvancements.api.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.core.advancement.objective.NumberObjective;
import killercreepr.cruxadvancements.core.advancement.objective.progress.NumberObjectiveProgress;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class NumberObjectiveProgressChangeEvent extends AbstractObjectiveEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    protected final @NotNull NumberObjective objective;
    protected final @NotNull NumberObjectiveProgress progress;
    protected int newProgress;
    protected final int oldProgress;
    public NumberObjectiveProgressChangeEvent(@NotNull UUID who,
                                              @NotNull CruxAdvancementManager<?> manager,
                                              @NotNull ObjectiveAdvancement advancement,
                                              @NotNull NumberObjective objective,
                                              @NotNull NumberObjectiveProgress progress,
                                              int newProgress, int oldProgress) {
        super(who, manager, advancement);
        this.objective = objective;
        this.progress = progress;
        this.newProgress = newProgress;
        this.oldProgress = oldProgress;
    }

    public @NotNull NumberObjective getObjective() {
        return objective;
    }

    public @NotNull NumberObjectiveProgress getProgress() {
        return progress;
    }

    public int getNewProgress() {
        return newProgress;
    }

    public void setNewProgress(int newProgress) {
        this.newProgress = newProgress;
    }

    public int getOldProgress() {
        return oldProgress;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
