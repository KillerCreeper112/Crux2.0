package killercreepr.cruxadvancements.advancement;

import killercreepr.cruxadvancements.advancement.objective.AdvancementObjective;
import killercreepr.cruxadvancements.advancement.objective.progress.ObjectiveProgression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public interface ObjectiveAdvancement extends CruxAdvancement{
    int getUpdateAdvancementPeriod();

    default  <T extends AdvancementObjective> void accept(@NotNull Class<T> type, @NotNull BiConsumer<String, T> consumer){
        getObjectives(type).forEach(consumer);
    }

    default  <T extends AdvancementObjective> @NotNull Map<String, T> getObjectives(@NotNull Class<T> type){
        Map<String, T> list = new HashMap<>();
        getObjectives().forEach((key, value) ->{
            if(type.isAssignableFrom(value.getClass())){
                list.put(key, type.cast(value));
            }
        });
        return list;
    }

    void setObjectiveProgress(@NotNull UUID uuid, @Nullable ObjectiveProgression progression);

    @Nullable AdvancementObjective getObjective(@NotNull String criterion);

    @NotNull ObjectiveProgression getObjectiveProgress(@NotNull UUID uuid);

    @Nullable ObjectiveProgression getObjectiveProgressIfPresent(@NotNull UUID uuid);

    @NotNull Map<String, AdvancementObjective> getObjectives();

    @NotNull Map<String, ObjectiveProgression> getObjectiveProgress();

    int getTotalProgress(@NotNull UUID who);
}
