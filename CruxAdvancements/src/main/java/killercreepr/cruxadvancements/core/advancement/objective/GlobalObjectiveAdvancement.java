package killercreepr.cruxadvancements.core.advancement.objective;

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

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

public class GlobalObjectiveAdvancement extends SimpleObjectiveAdvancement{
    public static final UUID USER_ID = UUID.nameUUIDFromBytes("_global_".getBytes(StandardCharsets.UTF_8));
    public GlobalObjectiveAdvancement(@NotNull Key key, @Nullable Key parentKey, @NotNull CruxAdvancementIcon icon, @NotNull CruxCriteria criteria, @Nullable CruxAdvanceReward reward, @NotNull Map<String, AdvancementObjective> objectives, int updateAdvancementPeriod) {
        super(key, parentKey, icon, criteria, reward, objectives, updateAdvancementPeriod);
    }

    public ObjectiveProgression getMainObjectiveProgress(){
        return objectiveProgress.computeIfAbsent(USER_ID.toString(), (u) -> buildObjectiveProgression());
    }

    @Override
    public void onSaving(CruxAdvancementManager<?> manager) {
        ((CruxAdvancementManager<ObjectiveAdvancement>)manager).saveProgress(USER_ID, this);
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
        return progressMap.computeIfAbsent(USER_ID.toString(), (s) -> super.buildProgress());
    }

    @Override
    public @NotNull CruxAdvancementProgress buildProgress(){
        if(getCriteria() instanceof ListCriteria c){
            return new GlobalListAdvancementProgress(c, getMainProgress());
        }
        if(getCriteria() instanceof NumberCriteria c){
            return new GlobalNumberAdvancementProgress(c, getMainProgress());
        }
        throw new UnsupportedOperationException(getCriteria() + " is an unsupported criteria!");
    }

    @Override
    public boolean isGranted(@NotNull UUID uuid) {
        return getMainProgress().isDone();
    }
}
