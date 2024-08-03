package killercreepr.cruxadvancements.data;

import killercreepr.cruxadvancements.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.advancement.objective.AdvancementObjective;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.BiConsumer;

public class AdvancementTracker {
    protected final Collection<TrackedAdvancement> trackedAdvancements = new HashSet<>();
    public void apply(@NotNull BiConsumer<CruxAdvancementManager<?>, ObjectiveAdvancement> consumer){
        for(TrackedAdvancement a : trackedAdvancements){
            consumer.accept(
                a.getManager(),
                a.getAdvancementOrThrow(ObjectiveAdvancement.class)
            );
        }
    }

    public <T extends AdvancementObjective> void apply(@NotNull Class<T> type,
                                                       @NotNull ObjectiveConsumer<T> consumer){
        for(TrackedAdvancement a : trackedAdvancements){
            CruxAdvancementManager<?> manager = a.getManager();
            ObjectiveAdvancement advancement = a.getObjective();
            a.getAdvancementOrThrow(ObjectiveAdvancement.class).getObjectives(type).forEach((criterion, objective) ->{
                consumer.accept(
                    manager,
                    advancement,
                    objective
                );
            });
        }
    }

    public interface ObjectiveConsumer<T extends AdvancementObjective>{
        void accept(
            @NotNull CruxAdvancementManager manager,
            @NotNull ObjectiveAdvancement advancement,
            @NotNull T objective
        );
    }
}
