package killercreepr.cruxadvancements.core.advancement.progress.global;

import killercreepr.crux.api.data.Holder;
import killercreepr.cruxadvancements.api.advancement.progress.CruxAdvancementProgress;
import killercreepr.cruxadvancements.api.advancement.progress.CruxCriterionProgress;
import killercreepr.cruxadvancements.core.advancement.criteria.ListCriteria;
import killercreepr.cruxadvancements.core.advancement.progress.ListAdvancementProgress;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GlobalListAdvancementProgress extends ListAdvancementProgress {
    protected final Holder<CruxAdvancementProgress> mainProgress;
    public GlobalListAdvancementProgress(@NotNull ListCriteria criteria, Holder<CruxAdvancementProgress> mainProgress) {
        super(criteria);
        this.mainProgress = mainProgress;
    }

    public GlobalListAdvancementProgress combine(CruxAdvancementProgress progress){
        if(!(progress instanceof ListAdvancementProgress list)) return this;
        progressMap.putAll(list.getProgressMap());
        this.setObtainedAt(list.getObtained());
        return this;
    }

    @Override
    public boolean checkAllGranted() {
        return ((ListAdvancementProgress)mainProgress.value()).checkAllGranted();
    }

    @Override
    public void onProgressChanged(@NotNull String name, @Nullable CruxCriterionProgress progress) {
        super.onProgressChanged(name, progress);
        if(progress == null || !progress.isDone()){
            mainProgress.value().revokeCriteria(name);
        }else{
            mainProgress.value().grantCriteria(name);
        }
    }
}
