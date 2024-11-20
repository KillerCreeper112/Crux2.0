package killercreepr.cruxadvancements.core.advancement.objective;

import killercreepr.cruxadvancements.api.advancement.criteria.CruxCriteria;
import killercreepr.cruxadvancements.api.advancement.icon.CruxAdvancementIcon;
import killercreepr.cruxadvancements.api.advancement.objective.AdvancementObjective;
import killercreepr.cruxadvancements.core.advancement.objective.progress.NumberObjectiveProgress;
import killercreepr.cruxadvancements.api.advancement.objective.progress.ObjectiveProgress;
import killercreepr.cruxadvancements.api.advancement.objective.progress.ObjectiveProgression;
import killercreepr.cruxadvancements.core.advancement.objective.progress.SimpleObjectiveProgression;
import killercreepr.cruxadvancements.api.advancement.reward.CruxAdvanceReward;
import killercreepr.cruxadvancements.api.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.core.advancement.SimpleAdvancement;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public class SimpleObjectiveAdvancement extends SimpleAdvancement implements ObjectiveAdvancement {
    protected final @NotNull Map<String, AdvancementObjective> objectives;
    protected final @NotNull Map<String, ObjectiveProgression> objectiveProgress = new HashMap<>();
    protected final int updateAdvancementPeriod;
    public SimpleObjectiveAdvancement(@NotNull Key key, @Nullable Key parentKey,
                                @NotNull CruxAdvancementIcon icon,
                                @NotNull CruxCriteria criteria,
                                @Nullable CruxAdvanceReward reward,
                                @NotNull Map<String, AdvancementObjective> objectives, int updateAdvancementPeriod) {
        super(key, parentKey, icon, criteria, reward);
        this.objectives = Collections.unmodifiableMap(objectives);
        this.updateAdvancementPeriod = updateAdvancementPeriod;
    }

    public int getUpdateAdvancementPeriod() {
        return updateAdvancementPeriod;
    }

    public <T extends AdvancementObjective> void accept(@NotNull Class<T> type, @NotNull BiConsumer<String, T> consumer){
        getObjectives(type).forEach(consumer);
    }

    public <T extends AdvancementObjective> @NotNull Map<String, T> getObjectives(@NotNull Class<T> type){
        Map<String, T> list = new HashMap<>();
        objectives.forEach((key, value) ->{
            if(type.isAssignableFrom(value.getClass())){
                list.put(key, type.cast(value));
            }
        });
        return list;
    }

    public void setObjectiveProgress(@NotNull UUID uuid, @Nullable ObjectiveProgression progression){
        if(progression==null) objectiveProgress.remove(uuid.toString());
        else objectiveProgress.put(uuid.toString(), progression);
    }

    public @Nullable AdvancementObjective getObjective(@NotNull String criterion){
        return objectives.get(criterion);
    }

    protected @NotNull ObjectiveProgression buildObjectiveProgression(){
        return new SimpleObjectiveProgression(this);
    }

    public @NotNull ObjectiveProgression getObjectiveProgress(@NotNull UUID uuid) {
        return objectiveProgress.computeIfAbsent(uuid.toString(), (u) -> buildObjectiveProgression());
    }

    public @Nullable ObjectiveProgression getObjectiveProgressIfPresent(@NotNull UUID uuid) {
        return objectiveProgress.get(uuid.toString());
    }

    public @NotNull Map<String, AdvancementObjective> getObjectives() {
        return objectives;
    }

    public @NotNull Map<String, ObjectiveProgression> getObjectiveProgress() {
        return objectiveProgress;
    }

    @Override
    public int getTotalProgress(@NotNull UUID who){
        int total = 0;
        ObjectiveProgression progression = getObjectiveProgressIfPresent(who);
        if(progression == null) return total;

        for(AdvancementObjective obj : getObjectives().values()){
            if(!(obj instanceof NumberObjective numberObjective)) continue;
            ObjectiveProgress progress = progression.getProgressIfPresent(obj.getCriterion());
            if(!(progress instanceof NumberObjectiveProgress pro)) continue;
            total += pro.getProgress();
        }
        return total;
    }
}
