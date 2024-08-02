package killercreepr.cruxadvancements;

import killercreepr.cruxadvancements.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.advancement.objective.BreakBlockObjective;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class Test implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        UUID who = p.getUniqueId();
        Block block = event.getBlock();
        Collection<ObjectiveAdvancement> tracked = new HashSet<>();
        for(ObjectiveAdvancement a : tracked){
            a.getObjectives(BreakBlockObjective.class).forEach((id, value) ->{
                value.trigger(
                    who, null, null, id,
                    a.getObjectiveProgress(who).getProgress(id),
                    block.getType()
                );
            });
        }
    }
}
