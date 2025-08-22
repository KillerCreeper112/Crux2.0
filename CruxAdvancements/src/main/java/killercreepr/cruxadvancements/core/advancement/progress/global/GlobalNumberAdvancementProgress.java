package killercreepr.cruxadvancements.core.advancement.progress.global;

import killercreepr.crux.api.data.Holder;
import killercreepr.cruxadvancements.api.advancement.progress.CriteriaResult;
import killercreepr.cruxadvancements.api.advancement.progress.CruxAdvancementProgress;
import killercreepr.cruxadvancements.core.advancement.criteria.NumberCriteria;
import killercreepr.cruxadvancements.core.advancement.progress.NumberAdvancementProgress;
import org.jetbrains.annotations.NotNull;

public class GlobalNumberAdvancementProgress extends NumberAdvancementProgress {
    protected final Holder<CruxAdvancementProgress> mainProgress;
    public GlobalNumberAdvancementProgress(@NotNull NumberCriteria criteria, Holder<CruxAdvancementProgress> mainProgress) {
        super(criteria);
        this.mainProgress = mainProgress;
    }


    public GlobalNumberAdvancementProgress combine(CruxAdvancementProgress progress){
        if(!(progress instanceof NumberAdvancementProgress list)) return this;
        this.progress = list.getProgress();
        this.setObtainedAt(list.getObtained());
        return this;
    }

    @Override
    public boolean checkAllGranted() {
        return ((NumberAdvancementProgress)mainProgress).checkAllGranted();
    }

    @Override
    public int getCriteriaMaxProgress() {
        return mainProgress.value().getCriteriaMaxProgress();
    }

    @Override
    public @NotNull CriteriaResult setCriteriaProgress(int amount) {
        CriteriaResult result = super.setCriteriaProgress(amount);
        if(result == CriteriaResult.UNCHANGED) return CriteriaResult.UNCHANGED;
        int difference = result.getNewProgress() - result.getPreviousProgress();
        mainProgress.value().setCriteriaProgress(mainProgress.value().getCriteriaProgress() + difference);
        return result;
    }
}
