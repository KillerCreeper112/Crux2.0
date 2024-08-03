package killercreepr.cruxadvancements;

import killercreepr.cruxadvancements.advancement.objective.BreakBlockObjective;
import killercreepr.cruxadvancements.data.AdvancementTracker;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.UUID;

public class Test implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        UUID who = p.getUniqueId();
        Block block = event.getBlock();
        AdvancementTracker tracker = null;
        tracker.apply(
            BreakBlockObjective.class, (manager, advancement, objective) -> {
                objective.trigger(
                    who, manager, advancement, block.getType()
                );
            }
        );
    }
}
