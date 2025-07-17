package killercreepr.cruxadvancements.core.listener;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.cruxadvancements.api.advancement.flag.CruxAdvancementFlag;
import killercreepr.cruxadvancements.api.event.CruxAdvancementGrantEvent;
import killercreepr.cruxadvancements.api.event.CruxAdvancementRevokeEvent;
import killercreepr.cruxadvancements.core.data.TrackedAdvancement;
import killercreepr.cruxadvancements.core.entity.memory.AdvancementHolder;
import killercreepr.cruxadvancements.crazy.advancement.CrazyAdvancement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class AdvancementGrantListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onCruxAdvancementGrant(CruxAdvancementGrantEvent event) {
        AdvancementHolder data = EntityMemory.getDataHolder(event.getWho(), AdvancementHolder.class);
        if(data==null) return;
        data.onAdvancementComplete(event.getManager(), event.getAdvancement());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCruxAdvancementRevoke(CruxAdvancementRevokeEvent event) {
        AdvancementHolder data = EntityMemory.getDataHolder(event.getWho(), AdvancementHolder.class);
        if(data==null) return;
        if(!(event.getAdvancement() instanceof CrazyAdvancement a) || !a.hasFlag(CruxAdvancementFlag.GLOBAL)) return;
        data.getAdvancementTracker().track(new TrackedAdvancement(
            event.getManager().key(), a.key(), true, System.currentTimeMillis()
        ));
    }

}
