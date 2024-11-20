package killercreepr.cruxblocks.api.block.active;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public interface ActiveCruxInteractable {
    @NotNull Event.Result interact(@NotNull PlayerInteractEvent event);
}
