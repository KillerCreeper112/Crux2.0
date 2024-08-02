package killercreepr.cruxadvancements.manager;

import killercreepr.crux.registry.KeyedRegistry;
import killercreepr.crux.registry.SimpleKeyedRegistry;
import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.advancement.progression.CriteriaResult;
import killercreepr.cruxadvancements.advancement.progression.CruxAdvancementProgress;
import killercreepr.cruxadvancements.event.*;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.UUID;

public abstract class SimpleAdvancementManager<T extends CruxAdvancement> implements CruxAdvancementManager<T> {
    protected final @NotNull Key key;
    protected final @NotNull KeyedRegistry<T> advancements = new SimpleKeyedRegistry<>();

    public SimpleAdvancementManager(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public void unloadProgress(@NotNull UUID uuid, @NotNull T... advancements){
        if(advancements.length == 0) advancements = (T[]) this.advancements.values().toArray(new CruxAdvancement[0]);
        for(T a : advancements){
            a.setProgress(uuid, null);
        }
    }

    @Override
    public void registerAdvancement(@NotNull T a){
        advancements.register(a);
    }

    @Override
    public void unregisterAdvancement(@NotNull T a){
        advancements.unregister(a);
    }

    @Override
    public @Nullable CruxAdvancementGrantEvent grantAdvancement(@NotNull UUID who, @NotNull T advancement){
        CruxAdvancementProgress progress = advancement.getProgress(who);
        if(progress.isDone()) return null;

        CruxAdvancementGrantEvent event = new CruxAdvancementGrantEvent(who, this, advancement);

        if(!event.callEvent()) return event;

        progress.grant();
        return event;
    }

    @Override
    public @Nullable CruxAdvancementRevokeEvent revokeAdvancement(@NotNull UUID who, @NotNull T advancement) {
        CruxAdvancementProgress progress = advancement.getProgress(who);
        if(!progress.isDone()) return null;

        CruxAdvancementRevokeEvent event = new CruxAdvancementRevokeEvent(who, this, advancement);

        if(!event.callEvent()) return event;

        progress.revoke();
        return event;
    }

    @Override
    public @Nullable CruxAdvancementCriteriaGrantEvent grantCriteria(@NotNull UUID who, @NotNull T advancement,
                                                                     @NotNull String... criteria) {
        CruxAdvancementProgress progress = advancement.getProgress(who);
        if(progress.isDone()) return null;

        CruxAdvancementCriteriaGrantEvent event = new CruxAdvancementCriteriaGrantEvent(
            who, this, advancement, criteria
        );

        if(!event.callEvent()) return event;
        criteria = event.getCriteriaToGrant();

        CriteriaResult result = progress.grantCriteria(criteria);
        if(result.wasCompleted()){
            CruxAdvancementGrantEvent grantEvent = new CruxAdvancementGrantEvent(who, this, advancement);
            if(!grantEvent.callEvent()){
                progress.revokeCriteria(result.getChangedCriteria().toArray(new String[0]));
                event.setCancelled(true);
            }
        }
        return event;
    }

    @Override
    public @Nullable CruxAdvancementCriteriaRevokeEvent revokeCriteria(@NotNull UUID who, @NotNull T advancement,
                                                                      @NotNull String... criteria) {
        CruxAdvancementProgress progress = advancement.getProgress(who);
        CruxAdvancementCriteriaRevokeEvent event = new CruxAdvancementCriteriaRevokeEvent(
            who, this, advancement, criteria
        );

        if(!event.callEvent()) return event;
        criteria = event.getCriteriaToRevoke();

        boolean had = progress.isDone();
        CriteriaResult result = progress.revokeCriteria(criteria);
        if(result.wasChanged()){
            if(had && !progress.isDone()){
                CruxAdvancementRevokeEvent revokeEvent = new CruxAdvancementRevokeEvent(who, this, advancement);
                if(!revokeEvent.callEvent()){
                    progress.grantCriteria(result.getChangedCriteria().toArray(new String[0]));
                    event.setCancelled(true);
                }
            }
        }
        return event;
    }

    @Override
    public @Nullable CruxAdvancementProgressChangeEvent setCriteriaProgress(@NotNull UUID who,
                                                                            @NotNull T advancement,
                                                                            int newProgress) {
        CruxAdvancementProgress progress = advancement.getProgress(who);

        if(progress.getCriteriaProgress() == newProgress) return null;
        if(newProgress >= progress.getCriteriaMaxProgress() && progress.isDone()){
            return null;
        }

        CruxAdvancementProgressChangeEvent event = new CruxAdvancementProgressChangeEvent(
            who, this, advancement, newProgress
        );

        if(!event.callEvent()) return event;
        newProgress = event.getNewProgress();

        boolean had = progress.isDone();
        CriteriaResult result = progress.setCriteriaProgress(newProgress);
        if(result.wasCompleted() && !had){
            CruxAdvancementGrantEvent grantEvent = new CruxAdvancementGrantEvent(who, this, advancement);
            if(!grantEvent.callEvent()){
                progress.setCriteriaProgress(result.getPreviousProgress());
                event.setCancelled(true);
            }
        }else if(result.wasChanged() && !result.wasCompleted() && had){
            CruxAdvancementRevokeEvent revokeEvent = new CruxAdvancementRevokeEvent(who, this, advancement);
            if(!revokeEvent.callEvent()){
                progress.setCriteriaProgress(result.getPreviousProgress());
                event.setCancelled(true);
            }
        }
        return event;
    }

    @Override
    public @NotNull KeyedRegistry<T> getAdvancements() {
        return advancements;
    }

    @Override
    public @Nullable T getAdvancement(@NotNull Key key) {
        return advancements.get(key);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return advancements.values().iterator();
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
