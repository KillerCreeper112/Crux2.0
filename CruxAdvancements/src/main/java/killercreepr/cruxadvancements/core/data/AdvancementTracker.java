package killercreepr.cruxadvancements.core.data;

import killercreepr.cruxadvancements.api.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.api.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import killercreepr.cruxadvancements.api.advancement.objective.AdvancementObjective;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.BiConsumer;

public class AdvancementTracker {
    protected final Collection<TrackedAdvancement> trackedAdvancements = new HashSet<>();
    protected final Collection<TrackedAdvancement> globalTracked = new HashSet<>();

    public void setTrackedAdvancements(@NotNull Collection<TrackedAdvancement> advancements){
        trackedAdvancements.clear();
        trackedAdvancements.addAll(advancements);
    }

    public void setGlobalTrackedAdvancements(@NotNull Collection<TrackedAdvancement> advancements){
        globalTracked.clear();
        globalTracked.addAll(advancements);
    }

    public boolean isTracking(TrackedAdvancement a){
        return trackedAdvancements.contains(a) || globalTracked.contains(a);
    }

    public boolean isTracking(Key manager, Key advancement){
        return isTracking(new TrackedAdvancement(manager, advancement, 0L));
    }

    public TrackedAdvancement getNonGlobalTrackedAdvancement(Key manager, Key advancement){
        for(var tracked : trackedAdvancements){
            if(tracked.getManagerKey().equals(manager) && tracked.getAdvancementKey().equals(advancement)) return tracked;
        }
        return null;
    }

    public Collection<TrackedAdvancement> getTrackedAdvancements() {
        return trackedAdvancements;
    }

    public Collection<TrackedAdvancement> getGlobalTrackedAdvancements() {
        return globalTracked;
    }

    public void untrackAll(){
        trackedAdvancements.clear();
    }

    public void untrackAllGlobal(){
        globalTracked.clear();
    }

    public void untrack(@NotNull TrackedAdvancement tracked){
        trackedAdvancements.remove(tracked);
        globalTracked.remove(tracked);
    }

    public void untrack(@NotNull CruxAdvancementManager<?> manager, @NotNull CruxAdvancement advancement){
        untrack(manager.key(), advancement.key());
    }

    public boolean untrack(@NotNull Key advancement){
        boolean x = trackedAdvancements.removeIf(d -> d.getAdvancementKey().equals(advancement));
        boolean x1 = globalTracked.removeIf(d -> d.getAdvancementKey().equals(advancement));
        return x || x1;
    }

    public boolean untrack(@NotNull CruxAdvancement advancement){
        return untrack(advancement.key());
    }

    public void untrack(@NotNull Key manager, @NotNull Key advancement){
        untrack(new TrackedAdvancement(manager, advancement, 0L));
    }

    public void track(@NotNull CruxAdvancementManager<?> manager, @NotNull CruxAdvancement advancement){
        track(manager.key(), advancement.key());
    }

    public void track(@NotNull Key manager, @NotNull Key advancement){
        track(new TrackedAdvancement(manager, advancement, System.currentTimeMillis()));
    }

    public void track(@NotNull TrackedAdvancement tracked){
        if(tracked.isGlobal()){
            globalTracked.add(tracked);
            return;
        }
        trackedAdvancements.add(tracked);
    }

    public Collection<TrackedAdvancement> getAllTracked(){
        Collection<TrackedAdvancement> list = new HashSet<>(trackedAdvancements);
        list.addAll(globalTracked);
        return list;
    }

    public void apply(@NotNull BiConsumer<CruxAdvancementManager<?>, ObjectiveAdvancement> consumer){
        for(TrackedAdvancement a : getAllTracked()){
            consumer.accept(
                a.getManager(),
                a.getAdvancementOrThrow(ObjectiveAdvancement.class)
            );
        }
    }

    public <T extends AdvancementObjective> void apply(@NotNull Class<T> type,
                                                       @NotNull ObjectiveConsumer<T> consumer){
        for(TrackedAdvancement a : getAllTracked()){
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
