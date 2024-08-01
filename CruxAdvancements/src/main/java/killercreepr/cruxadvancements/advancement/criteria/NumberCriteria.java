package killercreepr.cruxadvancements.advancement.criteria;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class NumberCriteria implements CruxCriteria {
    protected final int maxProgress;
    public NumberCriteria(int maxProgress) {
        this.maxProgress = maxProgress;
    }
    public int getMaxProgress() {
        return maxProgress;
    }

    @Override
    public boolean test(@NotNull Predicate<String> predicate) {
        return false;
    }
}
