package killercreepr.cruxadvancements.crazy.advancement;

import eu.endercentral.crazy_advancements.advancement.Advancement;
import eu.endercentral.crazy_advancements.advancement.criteria.Criteria;
import eu.endercentral.crazy_advancements.advancement.progress.AdvancementProgress;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import killercreepr.cruxadvancements.api.advancement.progress.CruxAdvancementProgress;
import killercreepr.cruxadvancements.api.event.*;
import killercreepr.cruxadvancements.core.advancement.criteria.ListCriteria;
import killercreepr.cruxadvancements.core.advancement.criteria.NumberCriteria;
import killercreepr.cruxadvancements.core.advancement.manager.SimpleAdvancementManager;
import killercreepr.cruxadvancements.core.advancement.progress.ListAdvancementProgress;
import killercreepr.cruxadvancements.crazy.util.CrazyUtil;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public abstract class CrazyAdvancementManager<T extends CrazyAdvancement> extends SimpleAdvancementManager<T> {
    protected final @NotNull AdvancementManager crazyManager;
    protected final @NotNull Map<Key, Advancement> crazyAdvancements = new HashMap<>();
    public CrazyAdvancementManager(@NotNull Key key, @NotNull AdvancementManager crazyManager) {
        super(key);
        this.crazyManager = crazyManager;
    }

    public @NotNull AdvancementManager getCrazyManager() {
        return crazyManager;
    }

    @Override
    public void unloadProgress(@NotNull UUID uuid, @NotNull T... advancements) {
        super.unloadProgress(uuid, advancements);
        if(advancements.length == 0) advancements = (T[]) this.advancements.values().toArray(new CrazyAdvancement[0]);
        for(T a : advancements){
            Advancement crazy = getCrazyAdvancement(a.key());
            if(crazy == null) continue;
            crazy.unloadProgress(uuid);
        }
    }

    @Override
    public @Nullable CruxAdvancementGrantEvent grantAdvancement(@NotNull UUID who, @NotNull T advancement) {
        CruxAdvancementGrantEvent event = super.grantAdvancement(who, advancement);
        if(event==null || event.isCancelled()) return event;
        crazyManager.grantAdvancement(who, getOrCreateCrazyAdvancement(advancement));
        return event;
    }

    @Override
    public @Nullable CruxAdvancementRevokeEvent revokeAdvancement(@NotNull UUID who, @NotNull T advancement) {
        CruxAdvancementRevokeEvent event = super.revokeAdvancement(who, advancement);
        if(event == null || event.isCancelled()) return event;
        crazyManager.revokeAdvancement(who, getOrCreateCrazyAdvancement(advancement));
        return event;
    }

    @Override
    public @Nullable CruxAdvancementCriteriaGrantEvent grantCriteria(@NotNull UUID who, @NotNull T advancement, @NotNull String... criteria) {
        CruxAdvancementCriteriaGrantEvent event = super.grantCriteria(who, advancement, criteria);
        if(event==null || event.isCancelled()) return event;
        crazyManager.grantCriteria(who, getOrCreateCrazyAdvancement(advancement), criteria);
        return event;
    }

    @Override
    public @Nullable CruxAdvancementCriteriaRevokeEvent revokeCriteria(@NotNull UUID who, @NotNull T advancement, @NotNull String... criteria) {
        CruxAdvancementCriteriaRevokeEvent event = super.revokeCriteria(who, advancement, criteria);
        if(event==null || event.isCancelled()) return event;
        crazyManager.revokeCriteria(who, getOrCreateCrazyAdvancement(advancement), criteria);
        return event;
    }

    @Override
    public @Nullable CruxAdvancementProgressChangeEvent setCriteriaProgress(@NotNull UUID who, @NotNull T advancement, int newProgress) {
        CruxAdvancementProgressChangeEvent event = super.setCriteriaProgress(who, advancement, newProgress);
        if(event==null || event.isCancelled()) return event;
        crazyManager.setCriteriaProgress(who, getOrCreateCrazyAdvancement(advancement), newProgress);
        return event;
    }

    public @NotNull Advancement getOrCreateCrazyAdvancement(@NotNull Key key){
        return getOrCreateCrazyAdvancement(
            Objects.requireNonNull(getAdvancement(key), "CrazyAdvancement, " + key + " does not exist!")
        );
    }

    public @NotNull Advancement getOrCreateCrazyAdvancement(@NotNull T crux){
        Advancement a = crazyAdvancements.get(crux.key());
        if(a != null) return a;
        a = createCrazyAdvancement(crux);
        registerCrazyAdvancement(a);
        return a;
    }

    public @NotNull Advancement createCrazyAdvancement(@NotNull T crux){
        Key parentCrux = crux.parent();
        Advancement parent = parentCrux == null ? null : getOrCreateCrazyAdvancement(
            Objects.requireNonNull(getAdvancement(parentCrux),
                crux.key() + " does not have its parent registered! (" + parentCrux + ")")
        );
        Advancement a = new Advancement(parent, CrazyUtil.toNameKey(crux.key()), crux.getDisplay().toCrazy(this), crux.toCrazyFlags());

        if(crux.getCriteria() instanceof ListCriteria c){
            a.setCriteria(new Criteria(c.getActionNames(), c.getRequirements()));
        }else if(crux.getCriteria() instanceof NumberCriteria c){
            a.setCriteria(new Criteria(c.getMaxProgress()));
        }
        return a;
    }

    public void loadCrazyProgress(@NotNull UUID uuid, @NotNull Advancement crazy, @NotNull CruxAdvancementProgress cruxProgress){
        AdvancementProgress progress = crazy.getProgress(uuid);
        if(cruxProgress instanceof ListAdvancementProgress a){
            a.getProgressMap().forEach((string, prog) ->{
                progress.grantCriteria(a.getAwardedCriteria().toArray(new String[0]));
            });
            return;
        }else{
            progress.setCriteriaProgress(cruxProgress.getCriteriaProgress());
        }

        //The timestamp on the CrazyAdvancements won't be accurate to Crux's, but
        //it should be fine. Any information should get gotten from Crux anyway.
        if(cruxProgress.isDone()){
            progress.grant();
        }
    }

    public void loadCrazyProgress(@NotNull Advancement crazy, @NotNull T crux){
        crux.getProgressMap().forEach((string, prog) ->{
            UUID uuid;
            try{
                uuid = UUID.fromString(string);
            }catch (IllegalArgumentException ignored){ return; }
            CruxAdvancementProgress cruxProgress = crux.getProgress(uuid);
            if(cruxProgress.isEmpty()) return;
            AdvancementProgress progress = crazy.getProgress(uuid);
            if(cruxProgress instanceof ListAdvancementProgress list){
                progress.grantCriteria(list.getAwardedCriteria().toArray(new String[0]));
                return;
            }
            progress.setCriteriaProgress(cruxProgress.getCriteriaProgress());
        });
    }

    public @Nullable Advancement getCrazyAdvancement(@NotNull Key key){
        return crazyAdvancements.get(key);
    }

    public void loadAllCrazyAdvancements(){
        for(T a : this){
            if(getCrazyAdvancement(a.key()) != null) continue;
            getOrCreateCrazyAdvancement(a);
        }
    }

    @Override
    public void unregisterAdvancement(@NotNull T a) {
        super.unregisterAdvancement(a);

        Advancement crazy = getCrazyAdvancement(a.key());
        if(crazy==null) return;
        unregisterCrazyAdvancement(crazy);
    }

    public void registerCrazyAdvancement(@NotNull Advancement crazy){
        crazyManager.addAdvancement(crazy);
        crazyAdvancements.put(CrazyUtil.toKey(crazy.getName()), crazy);
    }

    public void unregisterCrazyAdvancement(@NotNull Advancement crazy){
        crazyManager.removeAdvancement(crazy);
        crazyAdvancements.remove(CrazyUtil.toKey(crazy.getName()));
    }
}
