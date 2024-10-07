package killercreepr.cruxadvancements.advancement;

import killercreepr.cruxadvancements.advancement.criteria.CruxCriteria;
import killercreepr.cruxadvancements.advancement.icon.CruxAdvancementIcon;
import killercreepr.cruxadvancements.advancement.objective.AdvancementObjective;
import killercreepr.cruxadvancements.advancement.objective.progress.ObjectiveProgression;
import killercreepr.cruxadvancements.advancement.objective.progress.SimpleObjectiveProgression;
import killercreepr.cruxadvancements.advancement.reward.CruxAdvanceReward;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public class ObjectiveAdvancement extends SimpleAdvancement{
    protected final @NotNull Map<String, AdvancementObjective> objectives;
    protected final @NotNull Map<String, ObjectiveProgression> objectiveProgress = new HashMap<>();
    protected final int updateAdvancementPeriod;
    public ObjectiveAdvancement(@NotNull Key key, @Nullable Key parentKey,
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
}
