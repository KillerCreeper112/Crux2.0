package killercreepr.cruxadvancements.core.advancement.objective;

import killercreepr.crux.api.data.Holder;
import killercreepr.cruxadvancements.api.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.api.advancement.criteria.CruxCriteria;
import killercreepr.cruxadvancements.api.advancement.icon.CruxAdvancementIcon;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import killercreepr.cruxadvancements.api.advancement.objective.AdvancementObjective;
import killercreepr.cruxadvancements.api.advancement.objective.progress.ObjectiveProgression;
import killercreepr.cruxadvancements.api.advancement.progress.CruxAdvancementProgress;
import killercreepr.cruxadvancements.api.advancement.reward.CruxAdvanceReward;
import killercreepr.cruxadvancements.core.advancement.criteria.ListCriteria;
import killercreepr.cruxadvancements.core.advancement.criteria.NumberCriteria;
import killercreepr.cruxadvancements.core.advancement.progress.global.GlobalListAdvancementProgress;
import killercreepr.cruxadvancements.core.advancement.progress.global.GlobalNumberAdvancementProgress;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public class GlobalObjectiveAdvancement extends SimpleObjectiveAdvancement{
    public static final String USER_ID = "_global_";//UUID.nameUUIDFromBytes("_global_".getBytes(StandardCharsets.UTF_8));
    protected final Holder<CruxAdvancementProgress> mainProgressHolder = this::getMainProgress;
    public GlobalObjectiveAdvancement(@NotNull Key key, @Nullable Key parentKey, @NotNull CruxAdvancementIcon icon, @NotNull CruxCriteria criteria, @Nullable CruxAdvanceReward reward, @NotNull Map<String, AdvancementObjective> objectives, int updateAdvancementPeriod) {
        super(key, parentKey, icon, criteria, reward, objectives, updateAdvancementPeriod);
    }

    public ObjectiveProgression getMainObjectiveProgress(){
        return objectiveProgress.computeIfAbsent(USER_ID, (u) -> buildObjectiveProgression());
    }

    @Override
    public void onSaving(CruxAdvancementManager<?> manager) {
        ((CruxAdvancementManager<ObjectiveAdvancement>)manager).saveAllUserProgress(this);
    }

    @Override
    public void onLoading(CruxAdvancementManager<?> manager) {
        ((CruxAdvancementManager<ObjectiveAdvancement>)manager).loadAllUserProgress(this);
    }

    @Override
    public @NotNull ObjectiveProgression getObjectiveProgress(@NotNull UUID uuid) {
        return super.getObjectiveProgress(uuid);
    }

    public CruxAdvancementProgress getMainProgress(){
        return progressMap.computeIfAbsent(USER_ID, (s) -> super.buildProgress());
    }

    @Override
    public @NotNull CruxAdvancementProgress getProgress(@NotNull String id) {
        if(id.equals(USER_ID)) return getMainProgress();
        return super.getProgress(id);
    }

    @Override
    public void setProgress(@NotNull String id, @Nullable CruxAdvancementProgress progress) {
        if(id.equals(USER_ID) || progress instanceof GlobalListAdvancementProgress || progress instanceof GlobalNumberAdvancementProgress){
            super.setProgress(id, progress);
            return;
        }
        CruxAdvancementProgress newProgress;
        if(getCriteria() instanceof ListCriteria c){
            newProgress = new GlobalListAdvancementProgress(c, mainProgressHolder).combine(progress);
        }
        else if(getCriteria() instanceof NumberCriteria c){
            newProgress = new GlobalNumberAdvancementProgress(c, mainProgressHolder).combine(progress);
        }else throw new UnsupportedOperationException("Unsupported criteria " + getCriteria());

        super.setProgress(id, newProgress);
    }

    @Override
    public @NotNull CruxAdvancementProgress buildProgress(){
        if(getCriteria() instanceof ListCriteria c){
            return new GlobalListAdvancementProgress(c, mainProgressHolder);
        }
        if(getCriteria() instanceof NumberCriteria c){
            return new GlobalNumberAdvancementProgress(c, mainProgressHolder);
        }
        throw new UnsupportedOperationException(getCriteria() + " is an unsupported criteria!");
    }

    @Override
    public boolean isGranted(@NotNull UUID uuid) {
        return getMainProgress().isDone();
    }
}
