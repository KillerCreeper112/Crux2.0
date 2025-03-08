package killercreepr.cruxblocks.api.block.active;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ActiveCruxInteractable {
    @Nullable Event.Result interact(@NotNull PlayerInteractEvent event);
}
