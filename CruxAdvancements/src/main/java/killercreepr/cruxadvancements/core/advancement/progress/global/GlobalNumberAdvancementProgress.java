package killercreepr.cruxadvancements.core.advancement.progress.global;

import killercreepr.cruxadvancements.api.advancement.progress.CriteriaResult;
import killercreepr.cruxadvancements.api.advancement.progress.CruxAdvancementProgress;
import killercreepr.cruxadvancements.core.advancement.criteria.NumberCriteria;
import killercreepr.cruxadvancements.core.advancement.progress.NumberAdvancementProgress;
import org.jetbrains.annotations.NotNull;

public class GlobalNumberAdvancementProgress extends NumberAdvancementProgress {
    protected final CruxAdvancementProgress mainProgress;
    public GlobalNumberAdvancementProgress(@NotNull NumberCriteria criteria, CruxAdvancementProgress mainProgress) {
        super(criteria);
        this.mainProgress = mainProgress;
    }

    @Override
    public boolean checkAllGranted() {
        return ((NumberAdvancementProgress)mainProgress).checkAllGranted();
    }

    @Override
    public int getCriteriaMaxProgress() {
        return mainProgress.getCriteriaMaxProgress();
    }

    @Override
    public int getCriteriaProgress() {
        return mainProgress.getCriteriaProgress();
    }

    @Override
    public @NotNull CriteriaResult setCriteriaProgress(int amount) {
        CriteriaResult result = super.setCriteriaProgress(amount);
        if(result == CriteriaResult.UNCHANGED) return CriteriaResult.UNCHANGED;
        mainProgress.setCriteriaProgress(amount);
        return result;
    }
}
