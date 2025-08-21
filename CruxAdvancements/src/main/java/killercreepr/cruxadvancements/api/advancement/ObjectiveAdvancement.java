package killercreepr.cruxadvancements.api.advancement;

import killercreepr.cruxadvancements.api.advancement.objective.AdvancementObjective;
import killercreepr.cruxadvancements.api.advancement.objective.progress.ObjectiveProgression;
import org.jetbrains.annotations.ApiStatus;
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
    @ApiStatus.Experimental
    void setObjectiveProgress(@NotNull String id, @Nullable ObjectiveProgression progression);

    @Nullable AdvancementObjective getObjective(@NotNull String criterion);

    @NotNull ObjectiveProgression getObjectiveProgress(@NotNull UUID uuid);

    @Nullable ObjectiveProgression getObjectiveProgressIfPresent(@NotNull UUID uuid);
    @ApiStatus.Experimental
    @Nullable ObjectiveProgression getObjectiveProgressIfPresent(@NotNull String id);

    @NotNull Map<String, AdvancementObjective> getObjectives();

    @NotNull Map<String, ObjectiveProgression> getObjectiveProgress();

    int getTotalProgress(@NotNull UUID who);
}
