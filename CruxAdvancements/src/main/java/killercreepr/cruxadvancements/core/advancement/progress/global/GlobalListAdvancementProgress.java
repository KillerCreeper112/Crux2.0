package killercreepr.cruxadvancements.core.advancement.progress.global;

import killercreepr.cruxadvancements.api.advancement.progress.CruxAdvancementProgress;
import killercreepr.cruxadvancements.api.advancement.progress.CruxCriterionProgress;
import killercreepr.cruxadvancements.core.advancement.criteria.ListCriteria;
import killercreepr.cruxadvancements.core.advancement.progress.ListAdvancementProgress;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public void onProgressChanged(@NotNull String name, @Nullable CruxCriterionProgress progress) {
        super.onProgressChanged(name, progress);
        if(progress == null || !progress.isDone()){
            mainProgress.revokeCriteria(name);
        }else{
            mainProgress.grantCriteria(name);
        }
    }
}
