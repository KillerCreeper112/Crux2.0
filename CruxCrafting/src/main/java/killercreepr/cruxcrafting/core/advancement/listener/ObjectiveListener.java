package killercreepr.cruxcrafting.core.advancement.listener;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.cruxadvancements.core.entity.memory.AdvancementHolder;
import killercreepr.cruxcrafting.api.event.PlayerCruxCraftEvent;
import killercreepr.cruxcrafting.core.advancement.objective.CruxCraftObjective;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class ObjectiveListener implements Listener {
    private AdvancementHolder holder(@NotNull Player p){
        return EntityMemory.getOrCreateDataHolder(p, AdvancementHolder.class);
    }
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerCruxCraftEvent(PlayerCruxCraftEvent event) {
        if(!(event.getPlayer() instanceof Player p)) return;
        AdvancementHolder holder = holder(p);
        if(holder==null) return;
        holder.getAdvancementTracker().apply(CruxCraftObjective.class, (manager, advancement, objective) -> {
            objective.trigger(p.getUniqueId(), manager, advancement, event);
        });
    }

}
