package killercreepr.cruxadvancements.advancement.progression;

import killercreepr.crux.util.CruxMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Predicate;

public class NumberAdvancementProgress extends SimpleCriterionProgress implements CruxAdvancementProgress{
    protected final int maxProgress;
    protected int progress;

    public NumberAdvancementProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public @NotNull GrantCriteriaResult grantCriteria(@NotNull String... criteria) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull GenericResult revokeCriteria(@NotNull String... criteria) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull GrantCriteriaResult setCriteriaProgress(int amount) {
        amount = CruxMath.clamp(amount, 0, maxProgress);
        if(progress == amount) return GrantCriteriaResult.UNCHANGED;
        if(!update()) return GrantCriteriaResult.CHANGED;
        return isDone() ? GrantCriteriaResult.COMPLETED : GrantCriteriaResult.CHANGED;
    }

    @Override
    public int getCriteriaProgress() {
        return progress;
    }

    @Override
    public @Nullable CruxCriterionProgress getCriterionProgress(@NotNull String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Collection<String> getAwardedCriteria() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Collection<String> getRemainingCriteria() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getCriteriaMaxProgress() {
        return maxProgress;
    }

    @Override
    public int count(@NotNull Predicate<String> predicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Collection<String> assemble(@NotNull Predicate<String> predicate) {
        throw new UnsupportedOperationException();
    }

    protected boolean checkAllGranted(){
        return progress >= maxProgress;
    }

    protected boolean update(){
        if(checkAllGranted()){
            if(isDone()) return false;
            grant();
            return true;
        }
        if(!isDone()) return false;
        revoke();
        return true;
    }

}
