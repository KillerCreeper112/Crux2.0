package killercreepr.cruxadvancements.core.listener;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.cruxadvancements.api.event.CruxAdvancementGrantEvent;
import killercreepr.cruxadvancements.core.entity.memory.AdvancementHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class AdvancementGrantListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onCruxAdvancementGrant(CruxAdvancementGrantEvent event) {
        AdvancementHolder data = EntityMemory.getDataHolder(event.getWho(), AdvancementHolder.class);
        if(data==null) return;
        data.getAdvancementTracker().untrack(event.getAdvancement());
    }
}
