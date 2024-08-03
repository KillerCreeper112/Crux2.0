package killercreepr.cruxadvancements.listener;

import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.cruxadvancements.advancement.objective.impl.BreakBlockObjective;
import killercreepr.cruxadvancements.data.entity.AdvancementHolder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class ObjectiveListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        AdvancementHolder holder = holder(p);
        if(holder==null) return;

        Block block = event.getBlock();
        Material blockType = block.getType();

        holder.getAdvancementTracker().apply(BreakBlockObjective.class, (manager, advancement, objective) -> {
            objective.trigger(p.getUniqueId(), manager, advancement, blockType);
        });
    }

    private AdvancementHolder holder(@NotNull Player p){
        return EntityMemory.getOrCreateDataHolder(p, AdvancementHolder.class);
    }
}
