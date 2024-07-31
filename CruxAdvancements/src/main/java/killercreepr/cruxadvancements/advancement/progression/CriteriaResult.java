package killercreepr.cruxadvancements.advancement.progression;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

public class CriteriaResult {
    public static final CriteriaResult UNCHANGED = new CriteriaResult(Set.of(), false);
    protected final @NotNull Collection<String> changedCriteria;
    protected final boolean completed;

    protected final int previousProgress;
    protected final int newProgress;

    public CriteriaResult(@NotNull Collection<String> changedCriteria, boolean completed) {
        this.changedCriteria = changedCriteria;
        this.completed = completed;
        this.previousProgress = 0;
        this.newProgress = 0;
    }

    public CriteriaResult(int previousProgress, int newProgress, boolean completed) {
        this.previousProgress = previousProgress;
        this.newProgress = newProgress;
        this.completed = completed;
        this.changedCriteria = Set.of();
    }

    public boolean wasChanged(){
        return !changedCriteria.isEmpty() || previousProgress != newProgress;
    }

    public boolean wasCompleted(){
        return completed;
    }

    public @NotNull Collection<String> getChangedCriteria() {
        return changedCriteria;
    }

    public int getPreviousProgress() {
        return previousProgress;
    }

    public int getNewProgress() {
        return newProgress;
    }
}
