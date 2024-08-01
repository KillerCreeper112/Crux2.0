package killercreepr.cruxadvancements.crazy;

import eu.endercentral.crazy_advancements.advancement.Advancement;
import eu.endercentral.crazy_advancements.advancement.AdvancementReward;
import eu.endercentral.crazy_advancements.advancement.criteria.Criteria;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import killercreepr.cruxadvancements.advancement.criteria.ListCriteria;
import killercreepr.cruxadvancements.advancement.criteria.NumberCriteria;
import killercreepr.cruxadvancements.advancement.reward.CruxAdvanceReward;
import killercreepr.cruxadvancements.manager.SimpleAdvancementManager;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        a.setReward(new AdvancementReward() {
            @Override
            public void onGrant(Player player) {
                CruxAdvanceReward r = crux.reward();
                if(r==null) return;
                r.reward(player);
            }
        });

        if(crux.getCriteria() instanceof ListCriteria c){
            a.setCriteria(new Criteria(c.getActionNames(), c.getRequirements()));
        }else if(crux.getCriteria() instanceof NumberCriteria c){
            a.setCriteria(new Criteria(c.getMaxProgress()));
        }

        crazyAdvancements.put(crux.key(), a);
        return a;
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
