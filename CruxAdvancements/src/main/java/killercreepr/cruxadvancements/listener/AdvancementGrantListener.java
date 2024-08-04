package killercreepr.cruxadvancements.listener;

import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.cruxadvancements.data.entity.AdvancementHolder;
import killercreepr.cruxadvancements.event.CruxAdvancementGrantEvent;
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
