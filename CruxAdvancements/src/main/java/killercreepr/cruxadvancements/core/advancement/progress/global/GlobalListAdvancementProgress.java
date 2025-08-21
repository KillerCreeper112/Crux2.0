package killercreepr.cruxadvancements.core.advancement.progress.global;

import killercreepr.cruxadvancements.api.advancement.progress.CruxAdvancementProgress;
import killercreepr.cruxadvancements.api.advancement.progress.CruxCriterionProgress;
import killercreepr.cruxadvancements.core.advancement.criteria.ListCriteria;
import killercreepr.cruxadvancements.core.advancement.progress.ListAdvancementProgress;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class GlobalListAdvancementProgress extends ListAdvancementProgress {
    protected final CruxAdvancementProgress mainProgress;
    public GlobalListAdvancementProgress(@NotNull ListCriteria criteria, CruxAdvancementProgress mainProgress) {
        super(criteria);
        this.mainProgress = mainProgress;
    }

    @Override
    public boolean checkAllGranted() {
        return ((ListAdvancementProgress)mainProgress).checkAllGranted();
    }

    @Override
    public @NotNull Collection<String> getAwardedCriteria() {
        return mainProgress.getAwardedCriteria();
    }

    @Override
    public @NotNull Collection<String> getRemainingCriteria() {
        return mainProgress.getRemainingCriteria();
    }

    @Override
    public int getCriteriaMaxProgress() {
        return mainProgress.getCriteriaMaxProgress();
    }

    @Override
    public @Nullable CruxCriterionProgress getCriterionProgress(@NotNull String name) {
        return mainProgress.getCriterionProgress(name);
    }

    @Override
    public int getCriteriaProgress() {
        return mainProgress.getCriteriaProgress();
    }

    @Override
    public void onProgressChanged(@NotNull String name, @Nullable CruxCriterionProgress progress) {
        super.onProgressChanged(name, progress);
        if(progress == null || !progress.isDone()){
            mainProgress.revokeCriteria(name);
        }else{
            mainProgress.grantCriteria(name);
        }
    }
}
