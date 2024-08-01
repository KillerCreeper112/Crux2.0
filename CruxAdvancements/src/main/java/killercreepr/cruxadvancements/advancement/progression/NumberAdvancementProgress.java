package killercreepr.cruxadvancements.advancement.progression;

import killercreepr.crux.util.CruxMath;
import killercreepr.cruxadvancements.advancement.criteria.NumberCriteria;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Predicate;

public class NumberAdvancementProgress extends SimpleCriterionProgress implements CruxAdvancementProgress{
    protected final @NotNull NumberCriteria criteria;
    protected int progress;

    public NumberAdvancementProgress(@NotNull NumberCriteria criteria) {
        this.criteria = criteria;
    }

    public @NotNull NumberCriteria getCriteria() {
        return criteria;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public @NotNull CriteriaResult grantCriteria(@NotNull String... criteria) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull CriteriaResult revokeCriteria(@NotNull String... criteria) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull CriteriaResult setCriteriaProgress(int amount) {
        amount = CruxMath.clamp(amount, 0, criteria.getMaxProgress());
        if(progress == amount) return CriteriaResult.UNCHANGED;
        int previousProgress = this.progress;
        this.progress = amount;
        if(!update()) return new CriteriaResult(previousProgress, this.progress, false);
        return new CriteriaResult(previousProgress, this.progress, isDone());
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
        return criteria.getMaxProgress();
    }

    @Override
    public int count(@NotNull Predicate<String> predicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Collection<String> assemble(@NotNull Predicate<String> predicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        return progress==0;
    }

    protected boolean checkAllGranted(){
        return progress >= criteria.getMaxProgress();
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
