package killercreepr.cruxadvancements.crazy;

import eu.endercentral.crazy_advancements.advancement.Advancement;
import eu.endercentral.crazy_advancements.advancement.criteria.Criteria;
import eu.endercentral.crazy_advancements.advancement.progress.AdvancementProgress;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import killercreepr.cruxadvancements.advancement.criteria.ListCriteria;
import killercreepr.cruxadvancements.advancement.criteria.NumberCriteria;
import killercreepr.cruxadvancements.advancement.progression.CruxAdvancementProgress;
import killercreepr.cruxadvancements.advancement.progression.ListAdvancementProgress;
import killercreepr.cruxadvancements.manager.SimpleAdvancementManager;
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

    public @NotNull Advancement getOrCreateCrazyAdvancement(@NotNull Key key){
        return getOrCreateCrazyAdvancement(
            Objects.requireNonNull(getAdvancement(key), "CrazyAdvancement, " + key + " does not exist!")
        );
    }

    public @NotNull Advancement getOrCreateCrazyAdvancement(@NotNull T crux){
        Advancement a = crazyAdvancements.get(crux.key());
        if(a != null) return a;
        Key parentCrux = crux.parent();
        Advancement parent = parentCrux == null ? null : getOrCreateCrazyAdvancement(
            Objects.requireNonNull(getAdvancement(parentCrux),
                crux.key() + " does not have its parent registered! (" + parentCrux + ")")
        );
        a = new Advancement(parent, CrazyUtil.toNameKey(crux.key()), crux.getDisplay().toCrazy(this), crux.getFlags());

        if(crux.getCriteria() instanceof ListCriteria c){
            a.setCriteria(new Criteria(c.getActionNames(), c.getRequirements()));
        }else if(crux.getCriteria() instanceof NumberCriteria c){
            a.setCriteria(new Criteria(c.getMaxProgress()));
        }

        loadCrazyProgress(a, crux);
        crazyAdvancements.put(crux.key(), a);
        return a;
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
            Advancement crazy = getOrCreateCrazyAdvancement(a);
            crazyManager.addAdvancement(crazy);
        }
    }

    @Override
    public void unregisterAdvancement(@NotNull T a) {
        super.unregisterAdvancement(a);

        Advancement crazy = getCrazyAdvancement(a.key());
        if(crazy==null) return;
        crazyManager.removeAdvancement(crazy);
    }
}
