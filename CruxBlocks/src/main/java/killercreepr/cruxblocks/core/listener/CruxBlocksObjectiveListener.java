package killercreepr.cruxblocks.core.listener;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.cruxadvancements.core.entity.memory.AdvancementHolder;
import killercreepr.cruxblocks.api.block.context.BlockContext;
import killercreepr.cruxblocks.api.event.CruxBlockBreakEvent;
import killercreepr.cruxblocks.core.advancement.objective.CruxBreakBlockObjective;
import killercreepr.cruxblocks.core.mining.user.EntityMiner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class CruxBlocksObjectiveListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onCruxBlockBreak(CruxBlockBreakEvent event) {
        BlockContext ctx = event.getContext();
        if(!(ctx.getMiner() instanceof EntityMiner m) || !(m.getEntity() instanceof Player p)) return;
        AdvancementHolder holder = holder(p);
        if(holder==null) return;

        holder.getAdvancementTracker().apply(CruxBreakBlockObjective.class, (manager, advancement, objective) -> {
            objective.trigger(p.getUniqueId(), manager, advancement, event);
        });
    }
    private AdvancementHolder holder(@NotNull Player p){
        return EntityMemory.getOrCreateDataHolder(p, AdvancementHolder.class);
    }

}
